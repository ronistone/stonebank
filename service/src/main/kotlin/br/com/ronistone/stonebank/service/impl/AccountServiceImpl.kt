package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.commons.ValidationException
import br.com.ronistone.stonebank.entity.AccountEntity
import br.com.ronistone.stonebank.domain.AccountStatus
import br.com.ronistone.stonebank.entity.CustomerEntity
import br.com.ronistone.stonebank.entity.TransactionEntity
import br.com.ronistone.stonebank.repository.AccountRepository
import br.com.ronistone.stonebank.repository.CustomerRepository
import br.com.ronistone.stonebank.service.AccountService
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.domain.isGreaterThan
import br.com.ronistone.stonebank.domain.isLessThan
import br.com.ronistone.stonebank.entity.copyWithExample
import org.camunda.bpm.engine.RuntimeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class AccountServiceImpl(
    val accountRepository: AccountRepository,
    val customerRepository: CustomerRepository,
    val transactionService: TransactionService,
    val runtimeService: RuntimeService
): AccountService {

    companion object {
        private const val CUSTOMER_CREATION = "Customer_Creation"
    }

    override fun updateStatus(accountId: UUID, accountStatus: AccountStatus): AccountEntity {
        val account = accountRepository.findAccountById(accountId)

        if(account.isPresent) {

            return accountRepository.save(account.get().apply {
                status = accountStatus
            })

        }
        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    @Transactional
    override fun createAccount(accountEntity: AccountEntity): AccountEntity {
        if(accountEntity.customer == null || accountEntity.customer?.name == null || accountEntity.customer?.document == null) {
            throw ValidationException(Error.INVALID_CUSTOMER_INFORMATIONS)
        }

        val customerCreated = accountRepository.findByCustomerDocument(accountEntity.customer?.document!!)

        if(customerCreated != null) {
            throw ValidationException(Error.CUSTOMER_ALREADY_HAS_AN_ACCOUNT)
        }

        var customer = CustomerEntity(
            id = null,
            name = accountEntity.customer!!.name,
            document = accountEntity.customer!!.document
        )

        customer = customerRepository.save(customer)

        var newAccount = AccountEntity(
            id = null,
            amount = accountEntity.amount ?: BigDecimal.ZERO,
            customer = customer,
            status = AccountStatus.PENDING
        )
        newAccount = accountRepository.save(newAccount)

        runtimeService.startProcessInstanceByKey(
                CUSTOMER_CREATION,
                newAccount.id.toString(),
                mutableMapOf(Pair("document", newAccount.customer?.document)) as Map<String, Any>?
        )

        return newAccount
    }

    @Transactional
    override fun deposit(accountId: UUID, transactionEntityDeposit: TransactionEntity): AccountEntity {
        return doOperation(accountId, transactionEntityDeposit) { accountEntity: AccountEntity ->
            val transaction = transactionService.deposit(accountEntity, transactionEntityDeposit.copyWithExample(TransactionEntity(receiver = accountEntity)))
            accountEntity.amount = accountEntity.amount!!.plus(transaction.amount!!)
            accountRepository.save(accountEntity)
        }
    }

    @Transactional
    override fun withdraw(accountId: UUID, transactionEntityWithdraw: TransactionEntity): AccountEntity {

        return doOperation(accountId, transactionEntityWithdraw) { accountEntity: AccountEntity ->
            checkSufficientFunds(accountEntity, transactionEntityWithdraw)
            val transaction = transactionService.withdraw(accountEntity, transactionEntityWithdraw.copyWithExample(TransactionEntity( payer = accountEntity)))
            accountEntity.amount = accountEntity.amount!!.minus(transaction.amount!!)
            accountRepository.save(accountEntity)
        }
    }

    @Transactional
    override fun transfer(transactionEntityTransfer: TransactionEntity): AccountEntity {
        if(isInvalidAccountsToTransaction(transactionEntityTransfer)) {
            throw ValidationException(Error.TRANSACTIONS_INVALID)
        }
        val receiverAccount = accountRepository.findById(transactionEntityTransfer.receiver!!.id!!)

        if(receiverAccount.isEmpty) {
            throw ValidationException(Error.ACCOUNT_RECEIVER_NOT_FOUND)
        }

        val transactionOld = transactionService.getTransaction(transactionEntityTransfer.id!!)

        if(transactionOld.isEmpty) {
            throw ValidationException(Error.TRANSACTIONS_NOT_FOUND)
        }

        return doOperation(transactionEntityTransfer.payer!!.id!!, transactionEntityTransfer.copyWithExample(TransactionEntity(id=transactionOld.get().id))) { accountEntity: AccountEntity ->
            checkSufficientFunds(accountEntity, transactionEntityTransfer)
            val transaction = transactionService.transfer(accountEntity, transactionEntityTransfer.copyWithExample(TransactionEntity( payer = accountEntity)))
            accountEntity.amount = accountEntity.amount!!.minus(transaction.amount!!)

            receiverAccount.get().let {
                it.amount = it.amount!!.plus(transaction.amount!!)
                accountRepository.save(it)
            }

            accountRepository.save(accountEntity)
        }
    }

    private fun isInvalidAccountsToTransaction(transactionEntity: TransactionEntity) =
        hasInvalidAccount(transactionEntity.receiver) || hasInvalidAccount(transactionEntity.payer)
                || transactionEntity.receiver!!.id == transactionEntity.payer!!.id

    private fun hasInvalidAccount(accountEntity: AccountEntity?) = accountEntity?.id == null

    private fun doOperation(accountId: UUID, transactionEntity: TransactionEntity, operation: (AccountEntity) -> AccountEntity): AccountEntity {
        val account = accountRepository.findById(accountId)
        checkValidValue(transactionEntity)

        if(account.isPresent) {
            account.get().let {
                return operation(it)
            }
        }
        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    private fun checkValidValue(transactionEntity: TransactionEntity) {
        if (transactionEntity.amount == null || !transactionEntity.amount!!.isGreaterThan(BigDecimal.ZERO)) {
            throw ValidationException(Error.INVALID_AMOUNT)
        }
    }

    private fun checkSufficientFunds(
            it: AccountEntity,
            transactionEntity: TransactionEntity
    ) {
        if (it.amount!!.isLessThan(transactionEntity.amount!!)) {
            throw ValidationException(Error.INSUFFICIENT_FUNDS)
        }
    }

}