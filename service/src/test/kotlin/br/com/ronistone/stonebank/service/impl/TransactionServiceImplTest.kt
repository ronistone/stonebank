package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.entity.AccountEntity
import br.com.ronistone.stonebank.entity.TransactionEntity
import br.com.ronistone.stonebank.repository.TransactionRepository
import br.com.ronistone.stonebank.commons.Utils.Companion.any
import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.commons.ValidationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.kafka.core.KafkaTemplate
import java.math.BigDecimal
import java.util.UUID

class TransactionServiceImplTest {

    @Test
    fun `deposit invalid value throw exception`() {
        val transactionServiceImpl = getTransactionServiceImpl(mock(TransactionRepository::class.java))

        try {
            transactionServiceImpl.deposit(AccountEntity(id = UUID.randomUUID()), TransactionEntity(amount = BigDecimal.ZERO))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.DEPOSIT_NOT_POSITIVE)
        }
    }

    @Test
    fun `withdraw invalid value throw exception`() {
        val transactionServiceImpl = getTransactionServiceImpl(mock(TransactionRepository::class.java))

        try {
            transactionServiceImpl.withdraw(AccountEntity(id = UUID.randomUUID()), TransactionEntity(amount = BigDecimal.ZERO))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.WITHDRAW_NOT_POSITIVE)
        }
    }

    @Test
    fun `transfer invalid value throw exception`() {
        val transactionServiceImpl = getTransactionServiceImpl(mock(TransactionRepository::class.java))

        try {
            transactionServiceImpl.transfer(AccountEntity(id = UUID.randomUUID()), TransactionEntity(amount = BigDecimal.ZERO))
            fail("Expected validation exception")
        } catch (ex: ValidationException) {
            assertThat(ex.errors)
                .hasSize(1)
                .contains(Error.TRANSFER_NOT_POSITIVE)
        }
    }

    @Test
    fun `deposit success`() {
        val transactionRepository = mock(TransactionRepository::class.java)
        val transactionServiceImpl = getTransactionServiceImpl(transactionRepository)

        `when`(transactionRepository.save(any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity())

        transactionServiceImpl.deposit(AccountEntity(id = UUID.randomUUID()), TransactionEntity(amount = BigDecimal.TEN))

        verify(transactionRepository, times(1)).save(any(TransactionEntity::class.java))
    }

    @Test
    fun `withdraw success`() {
        val transactionRepository = mock(TransactionRepository::class.java)
        val transactionServiceImpl = getTransactionServiceImpl(transactionRepository)

        `when`(transactionRepository.save(any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity())

        transactionServiceImpl.withdraw(AccountEntity(id = UUID.randomUUID()), TransactionEntity(amount = BigDecimal.TEN))

        verify(transactionRepository, times(1)).save(any(TransactionEntity::class.java))
    }

    @Test
    fun `transfer success`() {
        val transactionRepository = mock(TransactionRepository::class.java)
        val transactionServiceImpl = getTransactionServiceImpl(transactionRepository)

        `when`(transactionRepository.save(any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity())

        transactionServiceImpl.transfer(AccountEntity(id = UUID.randomUUID()), TransactionEntity(amount = BigDecimal.TEN))

        verify(transactionRepository, times(1)).save(any(TransactionEntity::class.java))
    }

    @Test
    fun `createTransfer success`() {
        val transactionRepository = mock(TransactionRepository::class.java)
        val kafkaTemplate : KafkaTemplate<String, Any> = mock(KafkaTemplate::class.java) as KafkaTemplate<String, Any>
        val transactionServiceImpl = getTransactionServiceImpl(transactionRepository,
            kafkaTemplate
        )

        `when`(transactionRepository.save(any(TransactionEntity::class.java)))
            .thenReturn(TransactionEntity())

        transactionServiceImpl.createTransfer(UUID.randomUUID(), TransactionEntity())

        verify(transactionRepository, times(1)).save(any(TransactionEntity::class.java))
        verify(kafkaTemplate, times(1)).send(anyString(), any(Any::class.java))

    }

    fun getTransactionServiceImpl(transactionRepository: TransactionRepository, kafkaTemplate: KafkaTemplate<*, *>? = null): TransactionServiceImpl {
        return TransactionServiceImpl(transactionRepository,
            (kafkaTemplate ?: mock(KafkaTemplate::class.java)) as KafkaTemplate<Any, TransactionEntity>,
            "Topic"
        )
    }

}