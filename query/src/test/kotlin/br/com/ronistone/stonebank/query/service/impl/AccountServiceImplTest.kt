package br.com.ronistone.stonebank.query.service.impl

import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.commons.ValidationException
import br.com.ronistone.stonebank.query.model.AccountDocument
import br.com.ronistone.stonebank.query.repository.AccountRepository
import br.com.ronistone.stonebank.commons.Utils.Companion.any
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.Mockito
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
            Assertions.assertThat(ex.errors)
                    .hasSize(1)
                    .contains(Error.DOCUMENT_INVALID)
        }
    }

    @Test
    fun `getAccountByDocument success`() {
        val accountRepository = Mockito.mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        Mockito.`when`(accountRepository.findByCustomer_Document(Mockito.anyString()))
                .thenReturn(AccountDocument(id = UUID.randomUUID()))

        accountService.getAccountByDocument(DOCUMENT)
        Mockito.verify(accountRepository, Mockito.times(1)).findByCustomer_Document(Mockito.anyString())
    }


    @Test
    fun  `getBalance account not found throw exception`() {
        val accountRepository = Mockito.mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        Mockito.`when`(accountRepository.findById(any(UUID::class.java)))
                .thenReturn(Optional.empty())

        try {
            accountService.getBalance(UUID.randomUUID())
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            Assertions.assertThat(ex.errors)
                    .hasSize(1)
                    .contains(Error.ACCOUNT_NOT_FOUND)
        }
    }


    @Test
    fun  `getBalance account found success`() {
        val accountRepository = Mockito.mock(AccountRepository::class.java)
        val accountService = accountServiceImpl(accountRepository = accountRepository)

        val accountId = UUID.randomUUID()
        Mockito.`when`(accountRepository.findById(any(UUID::class.java)))
                .thenReturn(Optional.of(AccountDocument(id = accountId, amount = BigDecimal.ONE)))

        val account = accountService.getBalance(accountId)

        Mockito.verify(accountRepository, Mockito.times(1)).findById(any(UUID::class.java))
        assertThat(account)
                .isNotNull
                .hasFieldOrPropertyWithValue("amount", BigDecimal.ONE)
                .hasFieldOrPropertyWithValue("id", accountId)

    }


    private fun accountServiceImpl(
            accountRepository: AccountRepository? = null,
    ) = AccountServiceImpl(
            accountRepository = accountRepository ?: Mockito.mock(AccountRepository::class.java),
    )

}