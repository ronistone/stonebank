package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountDTO
import br.com.ronistone.stonebank.domain.Customer
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionDTO
import br.com.ronistone.stonebank.repository.AccountRepository
import br.com.ronistone.stonebank.repository.CustomerRepository
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.service.Utils.Companion.any
import br.com.ronistone.stonebank.service.Utils.Companion.eq
import br.com.ronistone.stonebank.service.commons.Error
import br.com.ronistone.stonebank.service.commons.ValidationException
import br.com.ronistone.stonebank.service.commons.toDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.math.BigDecimal
import java.util.Optional
import java.util.UUID

class AccountServiceImplTest {

    companion object {
        const val NAME = "Teste Name"
        const val DOCUMENT = "document number"
    }

    @Test
    fun `getAccountByDocument invalid document throw exception`() {
        val accountService = accountServiceImpl()

        try {
            accountService.getAccountByDocument(null)
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.DOCUMENT_INVALID)
        }
    }

    @Test
    fun `getAccountByDocument success`() {
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        `when`(accountRepository.findByCustomerDocument(anyString()))
            .thenReturn(Account(id = UUID.randomUUID()))

        accountService.getAccountByDocument(DOCUMENT)
        verify(accountRepository, times(1)).findByCustomerDocument(anyString())
    }

    @Test
    fun `createCustomer with invalid informations throw exception`() {
        val accountService = accountServiceImpl()

        try {
            accountService.createAccount(AccountDTO())
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.INVALID_CUSTOMER_INFORMATIONS)
        }
    }

    @Test
    fun `createCustomer customer already has account throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        `when`(accountRepository.findByCustomerDocument(anyString()))
            .thenReturn(Account(id = UUID.randomUUID()))

        try {
            accountService.createAccount(AccountDTO(
                name = NAME,
                document = DOCUMENT
            ))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.CUSTOMER_ALREADY_HAS_AN_ACCOUNT)
        }
    }

    @Test
    fun `createCustomer valid customer success`() {
        val customerRepository = mock(CustomerRepository::class.java)
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(customerRepository, accountRepository)

        `when`(customerRepository.save(any(Customer::class.java)))
            .thenReturn(Customer())
        `when`(accountRepository.save(any(Account::class.java)))
            .thenReturn(Account(id = UUID.randomUUID()))

        accountService.createAccount(AccountDTO(
            name = NAME,
            document = DOCUMENT
        ))

        verify(accountRepository, times(1)).save(any(Account::class.java))
        verify(customerRepository, times(1)).save(any(Customer::class.java))
    }

    @Test
    fun  `getBalance account not found throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.empty())

        try {
            accountService.getBalance(UUID.randomUUID())
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.ACCOUNT_NOT_FOUND)
        }
    }

    @Test
    fun  `getBalance account found success`() {
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        val accountId = UUID.randomUUID()
        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(Account(id = accountId, amount = BigDecimal.ONE)))

        val account = accountService.getBalance(accountId)

        verify(accountRepository, times(1)).findById(any(UUID::class.java))
        assertThat(account)
            .isNotNull
            .hasFieldOrPropertyWithValue("amount", BigDecimal.ONE)
            .hasFieldOrPropertyWithValue("id", accountId)

    }

    @Test
    fun  `getExtract account not found throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.empty())

        try {
            accountService.getExtract(UUID.randomUUID())
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.ACCOUNT_NOT_FOUND)
        }
    }

    @Test
    fun  `getExtract trasaction return null throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val accountId = UUID.randomUUID()
        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(Account(id = accountId, amount = BigDecimal.ONE)))
        `when`(transactionService.getExtract(any(Account::class.java)))
            .thenReturn(null)

        try {
            accountService.getExtract(accountId)
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.TRANSACTIONS_NOT_FOUND)
        }
    }

    @Test
    fun  `getExtract account found success`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val accountId = UUID.randomUUID()
        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(Account(id = accountId, amount = BigDecimal.ONE)))
        `when`(transactionService.getExtract(any(Account::class.java)))
            .thenReturn(listOf(Transaction()))

        val account = accountService.getExtract(accountId)

        verify(accountRepository, times(1)).findById(any(UUID::class.java))
        assertThat(account)
            .isNotNull
            .hasSize(1)

    }

    @Test
    fun `transfer invalid informations throw exception`() {
        val accountService = accountServiceImpl()

        try {
            accountService.transfer(TransactionDTO(payer = AccountDTO(id = UUID.randomUUID())))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.TRANSACTIONS_INVALID)
        }
    }

    @Test
    fun `transfer receiver account not found throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.empty())

        try {
            accountService.transfer(TransactionDTO(payer = AccountDTO(id = UUID.randomUUID()), receiver = AccountDTO(id = UUID.randomUUID())))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.ACCOUNT_RECEIVER_NOT_FOUND)
        }
    }

    @Test
    fun `transfer transaction not found throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        val receiver = AccountDTO(id = UUID.randomUUID())

        `when`(accountRepository.findById(eq(receiver.id!!)))
            .thenReturn(Optional.of(Account(id = receiver.id)))

        try {
            accountService.transfer(TransactionDTO(id = UUID.randomUUID(), payer = AccountDTO(id = UUID.randomUUID()), receiver = receiver))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.TRANSACTIONS_NOT_FOUND)
        }
    }

    @Test
    fun `transfer invalid transfer amount throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val receiver = AccountDTO(id = UUID.randomUUID())

        `when`(accountRepository.findById(eq(receiver.id!!)))
            .thenReturn(Optional.of(Account(id = receiver.id)))
        `when`(transactionService.getTransaction(any(UUID::class.java)))
            .thenReturn(Optional.of(Transaction()))

        try {
            accountService.transfer(TransactionDTO(id = UUID.randomUUID(), payer = AccountDTO(id = UUID.randomUUID()), receiver = receiver))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.INVALID_AMOUNT)
        }
    }

    @Test
    fun `transfer success throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val receiver = Account(id = UUID.randomUUID(), amount = BigDecimal.ZERO, customer = Customer())
        val payer = Account(id = UUID.randomUUID(), amount = BigDecimal.TEN, customer = Customer())

        `when`(accountRepository.findById(eq(receiver.id!!)))
            .thenReturn(Optional.of(receiver))

        `when`(accountRepository.findById(eq(payer.id!!)))
            .thenReturn(Optional.of(payer))

        `when`(transactionService.transfer(eq(payer), any(Transaction::class.java)))
            .thenReturn(Transaction(amount = BigDecimal.TEN))

        `when`(accountRepository.save(any(Account::class.java)))
            .thenReturn(Account(id = UUID.randomUUID()))

        `when`(transactionService.getTransaction(any(UUID::class.java)))
            .thenReturn(Optional.of(Transaction()))

        accountService.transfer(TransactionDTO(id = UUID.randomUUID(), payer = AccountDTO(id = payer.id!!), receiver = receiver.toDTO(), amount = BigDecimal.TEN))

        verify(accountRepository, times(2)).save(any(Account::class.java))
    }

    @Test
    fun `deposit account not found throw exception`() {
        val accountService = accountServiceImpl()

        val receiver = AccountDTO(id = UUID.randomUUID())

        try {
            accountService.deposit(UUID.randomUUID(), TransactionDTO( receiver = receiver, amount = BigDecimal.TEN))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.ACCOUNT_NOT_FOUND)
        }
    }

    @Test
    fun `deposit valid success`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val receiver = AccountDTO(id = UUID.randomUUID())

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(Account(id = UUID.randomUUID(), amount = BigDecimal.ZERO)))
        `when`(transactionService.deposit(any(Account::class.java), any(Transaction::class.java)))
            .thenReturn(Transaction(amount = BigDecimal.TEN))
        `when`(accountRepository.save(any(Account::class.java)))
            .thenReturn(Account(id = UUID.randomUUID()))

        accountService.deposit(UUID.randomUUID(), TransactionDTO( receiver = receiver, amount = BigDecimal.TEN))

        verify(accountRepository, times(1)).save(any(Account::class.java))
    }

    @Test
    fun `withdraw valid success`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val receiver = AccountDTO(id = UUID.randomUUID())

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(Account(id = UUID.randomUUID(), amount = BigDecimal.TEN)))
        `when`(transactionService.withdraw(any(Account::class.java), any(Transaction::class.java)))
            .thenReturn(Transaction(amount = BigDecimal.TEN))
        `when`(accountRepository.save(any(Account::class.java)))
            .thenReturn(Account(id = UUID.randomUUID()))

        accountService.withdraw(UUID.randomUUID(), TransactionDTO( receiver = receiver, amount = BigDecimal.TEN))

        verify(accountRepository, times(1)).save(any(Account::class.java))
    }

    @Test
    fun `withdraw insufficient funds throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val receiver = AccountDTO(id = UUID.randomUUID())

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(Account(id = UUID.randomUUID(), amount = BigDecimal.ZERO)))
        `when`(transactionService.deposit(any(Account::class.java), any(Transaction::class.java)))
            .thenReturn(Transaction(amount = BigDecimal.TEN))

        try {
            accountService.withdraw(UUID.randomUUID(), TransactionDTO(receiver = receiver, amount = BigDecimal.TEN))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.INSUFFICIENT_FUNDS)
        }
    }

    private fun accountServiceImpl(
        customerRepository: CustomerRepository? = null,
        accountRepository: AccountRepository? = null,
        transactionService: TransactionService? = null
    ) = AccountServiceImpl(
        customerRepository = customerRepository ?: mock(CustomerRepository::class.java),
        accountRepository = accountRepository ?: mock(AccountRepository::class.java),
        transactionService = transactionService ?: mock(TransactionService::class.java)
    )

}