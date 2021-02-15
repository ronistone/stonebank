package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountStatus
import br.com.ronistone.stonebank.domain.Customer
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.repository.AccountRepository
import br.com.ronistone.stonebank.repository.CustomerRepository
import br.com.ronistone.stonebank.service.AccountService
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.service.commons.Error
import br.com.ronistone.stonebank.service.commons.ValidationException
import br.com.ronistone.stonebank.service.commons.copyWithExample
import br.com.ronistone.stonebank.service.commons.isGreaterThan
import br.com.ronistone.stonebank.service.commons.isLessThan
import br.com.ronistone.stonebank.service.commons.toEntity
import org.camunda.bpm.engine.RuntimeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap

@Service
class AccountServiceImpl(
    val accountRepository: AccountRepository,
    val customerRepository: CustomerRepository,
    val transactionService: TransactionService,
    val runtimeService: RuntimeService
): AccountService {

    companion object {
        private val CUSTOMER_CREATION = "Customer_Creation"
    }

    override fun updateStatus(accountId: UUID, accountStatus: AccountStatus): Account {
        val account = accountRepository.findAccountById(accountId)

        if(account.isPresent) {

            return accountRepository.save(account.get().apply {
                status = accountStatus
            })

        }
        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    override fun getAccountByDocument(document: String?): Account {
        if(document == null) {
            throw ValidationException(Error.DOCUMENT_INVALID)
        }
        return accountRepository.findByCustomerDocument(document) ?: throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    @Transactional
    override fun createAccount(account: Account): Account {
        if(account.customer == null || account.customer?.name == null || account.customer?.document == null) {
            throw ValidationException(Error.INVALID_CUSTOMER_INFORMATIONS)
        }

        val customerCreated = accountRepository.findByCustomerDocument(account.customer?.document!!)

        if(customerCreated != null) {
            throw ValidationException(Error.CUSTOMER_ALREADY_HAS_AN_ACCOUNT)
        }

        var customer = Customer(
            id = null,
            name = account.customer!!.name,
            document = account.customer!!.document
        )

        customer = customerRepository.save(customer)

        var newAccount = Account(
            id = null,
            amount = account.amount ?: BigDecimal.ZERO,
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

    override fun getBalance(accountId: UUID): Account {
        val account = accountRepository.findById(accountId)

        if(account.isPresent) {
            return Account(
                id = accountId,
                amount = account.get().amount
            )
        }

        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    override fun getExtract(accountId: UUID): List<Transaction> {
        val account = accountRepository.findById(accountId)

        if(account.isPresent) {
            return transactionService.getExtract(account.get())
                ?: throw ValidationException(Error.TRANSACTIONS_NOT_FOUND)
        }
        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    @Transactional
    override fun deposit(accountId: UUID, transactionDeposit: Transaction): Account {
        return doOperation(accountId, transactionDeposit) { account: Account ->
            val transaction = transactionService.deposit(account, transactionDeposit.copyWithExample(Transaction(receiver = account)))
            account.amount = account.amount!!.plus(transaction.amount!!)
            accountRepository.save(account)
        }
    }

    @Transactional
    override fun withdraw(accountId: UUID, transactionWithdraw: Transaction): Account {

        return doOperation(accountId, transactionWithdraw) { account: Account ->
            checkSufficientFunds(account, transactionWithdraw)
            val transaction = transactionService.withdraw(account, transactionWithdraw.copyWithExample(Transaction( payer = account)))
            account.amount = account.amount!!.minus(transaction.amount!!)
            accountRepository.save(account)
        }
    }

    @Transactional
    override fun transfer(transactionTransfer: Transaction): Account {
        if(isInvalidAccountsToTransaction(transactionTransfer)) {
            throw ValidationException(Error.TRANSACTIONS_INVALID)
        }
        val receiverAccount = accountRepository.findById(transactionTransfer.receiver!!.id!!)

        if(receiverAccount.isEmpty) {
            throw ValidationException(Error.ACCOUNT_RECEIVER_NOT_FOUND)
        }

        val transactionOld = transactionService.getTransaction(transactionTransfer.id!!)

        if(transactionOld.isEmpty) {
            throw ValidationException(Error.TRANSACTIONS_NOT_FOUND)
        }

        return doOperation(transactionTransfer.payer!!.id!!, transactionTransfer.copyWithExample(Transaction(id=transactionOld.get().id))) { account: Account ->
            checkSufficientFunds(account, transactionTransfer)
            val transaction = transactionService.transfer(account, transactionTransfer.copyWithExample(Transaction( payer = account)))
            account.amount = account.amount!!.minus(transaction.amount!!)

            receiverAccount.get().let {
                it.amount = it.amount!!.plus(transaction.amount!!)
                accountRepository.save(it)
            }

            accountRepository.save(account)
        }
    }

    private fun isInvalidAccountsToTransaction(transaction: Transaction) =
        hasInvalidAccount(transaction.receiver) || hasInvalidAccount(transaction.payer)
                || transaction.receiver!!.id == transaction.payer!!.id

    private fun hasInvalidAccount(account: Account?) = account?.id == null

    private fun doOperation(accountId: UUID, transaction: Transaction, operation: (Account) -> Account): Account {
        val account = accountRepository.findById(accountId)
        checkValidValue(transaction)

        if(account.isPresent) {
            account.get().let {
                return operation(it)
            }
        }
        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    private fun checkValidValue(transaction: Transaction) {
        if (transaction.amount == null || !transaction.amount!!.isGreaterThan(BigDecimal.ZERO)) {
            throw ValidationException(Error.INVALID_AMOUNT)
        }
    }

    private fun checkSufficientFunds(
        it: Account,
        transaction: Transaction
    ) {
        if (it.amount!!.isLessThan(transaction.amount!!)) {
            throw ValidationException(Error.INSUFFICIENT_FUNDS)
        }
    }

}