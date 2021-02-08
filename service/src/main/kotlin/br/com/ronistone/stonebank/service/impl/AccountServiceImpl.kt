package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountDTO
import br.com.ronistone.stonebank.domain.Customer
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionDTO
import br.com.ronistone.stonebank.repository.AccountRepository
import br.com.ronistone.stonebank.repository.CustomerRepository
import br.com.ronistone.stonebank.service.AccountService
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.service.commons.Error
import br.com.ronistone.stonebank.service.commons.ValidationException
import br.com.ronistone.stonebank.service.commons.copyWithExample
import br.com.ronistone.stonebank.service.commons.isGreaterThan
import br.com.ronistone.stonebank.service.commons.isLessThan
import br.com.ronistone.stonebank.service.commons.toDTO
import br.com.ronistone.stonebank.service.commons.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class AccountServiceImpl(
    val accountRepository: AccountRepository,
    val customerRepository: CustomerRepository,
    val transactionService: TransactionService
): AccountService {

    override fun getAccountByDocument(document: String?): Account {
        if(document == null) {
            throw ValidationException(Error.DOCUMENT_INVALID)
        }
        return accountRepository.findByCustomerDocument(document) ?: throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    override fun createAccount(accountDTO: AccountDTO): Account {
        if(accountDTO.name == null || accountDTO.document == null) {
            throw ValidationException(Error.INVALID_CUSTOMER_INFORMATIONS)
        }

        val customerCreated = accountRepository.findByCustomerDocument(accountDTO.document!!)

        if(customerCreated != null) {
            throw ValidationException(Error.CUSTOMER_ALREADY_HAS_AN_ACCOUNT)
        }

        var customer = Customer(
            id = null,
            name = accountDTO.name!!,
            document = accountDTO.document!!
        )

        customer = customerRepository.save(customer)

        val account = Account(
            id = null,
            amount = accountDTO.amount ?: BigDecimal.ZERO,
            customer = customer
        )

        return accountRepository.save(account)
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
    override fun deposit(accountId: UUID, transactionDTO: TransactionDTO): Account {
        return doOperation(accountId, transactionDTO) { account: Account ->
            val transaction = transactionService.deposit(account, transactionDTO.copyWithExample(TransactionDTO( receiver = account.toDTO())).toEntity())
            account.amount = account.amount!!.plus(transaction.amount!!)
            accountRepository.save(account)
        }
    }

    @Transactional
    override fun withdraw(accountId: UUID, transactionDTO: TransactionDTO): Account {

        return doOperation(accountId, transactionDTO) { account: Account ->
            checkSufficientFunds(account, transactionDTO)
            val transaction = transactionService.withdraw(account, transactionDTO.copyWithExample(TransactionDTO( payer = account.toDTO())).toEntity())
            account.amount = account.amount!!.minus(transaction.amount!!)
            accountRepository.save(account)
        }
    }

    @Transactional
    override fun transfer(accountId: UUID, transactionDTO: TransactionDTO): Account {
        if(transactionDTO.receiver == null || transactionDTO.receiver!!.id == null || transactionDTO.receiver!!.id == accountId) {
            throw ValidationException(Error.TRANSACTIONS_INVALID)
        }
        val receiverAccount = accountRepository.findById(transactionDTO.receiver!!.id!!)

        if(receiverAccount.isEmpty) {
            throw ValidationException(Error.ACCOUNT_RECEIVER_NOT_FOUND)
        }

        return doOperation(accountId, transactionDTO) { account: Account ->
            checkSufficientFunds(account, transactionDTO)
            val transaction = transactionService.transfer(account, transactionDTO.copyWithExample(TransactionDTO( payer = account.toDTO())).toEntity())
            account.amount = account.amount!!.minus(transaction.amount!!)

            receiverAccount.get().let {
                it.amount = it.amount!!.plus(transaction.amount!!)
                accountRepository.save(it)
            }

            accountRepository.save(account)
        }
    }

    private fun doOperation(accountId: UUID, transactionDTO: TransactionDTO, operation: (Account) -> Account): Account {
        val account = accountRepository.findById(accountId)
        checkValidValue(transactionDTO)

        if(account.isPresent) {
            account.get().let {
                return operation(it)
            }
        }
        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }

    private fun checkValidValue(transactionDTO: TransactionDTO) {
        if (transactionDTO.amount == null || !transactionDTO.amount!!.isGreaterThan(BigDecimal.ZERO)) {
            throw ValidationException(Error.INVALID_AMOUNT)
        }
    }

    private fun checkSufficientFunds(
        it: Account,
        transactionDTO: TransactionDTO
    ) {
        if (it.amount!!.isLessThan(transactionDTO.amount!!)) {
            throw ValidationException(Error.INSUFFICIENT_FUNDS)
        }
    }

}