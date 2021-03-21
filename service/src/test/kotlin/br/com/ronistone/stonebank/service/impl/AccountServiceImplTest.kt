package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.entity.AccountEntity
import br.com.ronistone.stonebank.entity.CustomerEntity
import br.com.ronistone.stonebank.entity.TransactionEntity
import br.com.ronistone.stonebank.repository.AccountRepository
import br.com.ronistone.stonebank.repository.CustomerRepository
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.commons.Utils.Companion.any
import br.com.ronistone.stonebank.commons.Utils.Companion.eq
import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.commons.ValidationException
import org.assertj.core.api.Assertions.assertThat
import org.camunda.bpm.engine.RuntimeService
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
    fun `createCustomer with invalid informations throw exception`() {
        val accountService = accountServiceImpl()

        try {
            accountService.createAccount(AccountEntity())
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
            .thenReturn(AccountEntity(id = UUID.randomUUID()))

        try {
            accountService.createAccount(AccountEntity( customer = CustomerEntity(
                name = NAME,
                document = DOCUMENT
            )))
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

        `when`(customerRepository.save(any(CustomerEntity::class.java)))
            .thenReturn(CustomerEntity())
        `when`(accountRepository.save(any(AccountEntity::class.java)))
            .thenReturn(AccountEntity(id = UUID.randomUUID()))

        accountService.createAccount(AccountEntity(customer = CustomerEntity(
            name = NAME,
            document = DOCUMENT
        )))

        verify(accountRepository, times(1)).save(any(AccountEntity::class.java))
        verify(customerRepository, times(1)).save(any(CustomerEntity::class.java))
    }

    @Test
    fun `transfer invalid informations throw exception`() {
        val accountService = accountServiceImpl()

        try {
            accountService.transfer(TransactionEntity(payer = AccountEntity(id = UUID.randomUUID())))
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
            accountService.transfer(TransactionEntity(payer = AccountEntity(id = UUID.randomUUID()), receiver = AccountEntity(id = UUID.randomUUID())))
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

        val receiver = AccountEntity(id = UUID.randomUUID())

        `when`(accountRepository.findById(eq(receiver.id!!)))
            .thenReturn(Optional.of(AccountEntity(id = receiver.id)))

        try {
            accountService.transfer(TransactionEntity(id = UUID.randomUUID(), payer = AccountEntity(id = UUID.randomUUID()), receiver = receiver))
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

        val receiver = AccountEntity(id = UUID.randomUUID())

        `when`(accountRepository.findById(eq(receiver.id!!)))
            .thenReturn(Optional.of(AccountEntity(id = receiver.id)))
        `when`(transactionService.getTransaction(any(UUID::class.java)))
            .thenReturn(Optional.of(TransactionEntity()))

        try {
            accountService.transfer(TransactionEntity(id = UUID.randomUUID(), payer = AccountEntity(id = UUID.randomUUID()), receiver = receiver))
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

        val receiver = AccountEntity(id = UUID.randomUUID(), amount = BigDecimal.ZERO, customer = CustomerEntity())
        val payer = AccountEntity(id = UUID.randomUUID(), amount = BigDecimal.TEN, customer = CustomerEntity())

        `when`(accountRepository.findById(eq(receiver.id!!)))
            .thenReturn(Optional.of(receiver))

        `when`(accountRepository.findById(eq(payer.id!!)))
            .thenReturn(Optional.of(payer))

        `when`(transactionService.transfer(eq(payer), any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity(amount = BigDecimal.TEN))

        `when`(accountRepository.save(any(AccountEntity::class.java)))
            .thenReturn(AccountEntity(id = UUID.randomUUID()))

        `when`(transactionService.getTransaction(any(UUID::class.java)))
            .thenReturn(Optional.of(TransactionEntity()))

        accountService.transfer(TransactionEntity(id = UUID.randomUUID(), payer = AccountEntity(id = payer.id!!), receiver = receiver, amount = BigDecimal.TEN))

        verify(accountRepository, times(2)).save(any(AccountEntity::class.java))
    }

    @Test
    fun `deposit account not found throw exception`() {
        val accountService = accountServiceImpl()

        val receiver = AccountEntity(id = UUID.randomUUID())

        try {
            accountService.deposit(UUID.randomUUID(), TransactionEntity( receiver = receiver, amount = BigDecimal.TEN))
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

        val receiver = AccountEntity(id = UUID.randomUUID())

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(AccountEntity(id = UUID.randomUUID(), amount = BigDecimal.ZERO)))
        `when`(transactionService.deposit(any(AccountEntity::class.java), any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity(amount = BigDecimal.TEN))
        `when`(accountRepository.save(any(AccountEntity::class.java)))
            .thenReturn(AccountEntity(id = UUID.randomUUID()))

        accountService.deposit(UUID.randomUUID(), TransactionEntity( receiver = receiver, amount = BigDecimal.TEN))

        verify(accountRepository, times(1)).save(any(AccountEntity::class.java))
    }

    @Test
    fun `withdraw valid success`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val receiver = AccountEntity(id = UUID.randomUUID())

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(AccountEntity(id = UUID.randomUUID(), amount = BigDecimal.TEN)))
        `when`(transactionService.withdraw(any(AccountEntity::class.java), any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity(amount = BigDecimal.TEN))
        `when`(accountRepository.save(any(AccountEntity::class.java)))
            .thenReturn(AccountEntity(id = UUID.randomUUID()))

        accountService.withdraw(UUID.randomUUID(), TransactionEntity( receiver = receiver, amount = BigDecimal.TEN))

        verify(accountRepository, times(1)).save(any(AccountEntity::class.java))
    }

    @Test
    fun `withdraw insufficient funds throw exception`() {
        val accountRepository = mock(AccountRepository::class.java)
        val transactionService = mock(TransactionService::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository, transactionService = transactionService)

        val receiver = AccountEntity(id = UUID.randomUUID())

        `when`(accountRepository.findById(any(UUID::class.java)))
            .thenReturn(Optional.of(AccountEntity(id = UUID.randomUUID(), amount = BigDecimal.ZERO)))
        `when`(transactionService.deposit(any(AccountEntity::class.java), any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity(amount = BigDecimal.TEN))

        try {
            accountService.withdraw(UUID.randomUUID(), TransactionEntity(receiver = receiver, amount = BigDecimal.TEN))
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
        transactionService = transactionService ?: mock(TransactionService::class.java),
        runtimeService = mock(RuntimeService::class.java)
    )

}