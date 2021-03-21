package br.com.ronistone.stonebank.query.service.impl

import br.com.ronistone.stonebank.commons.Utils.Companion.any
import br.com.ronistone.stonebank.query.model.TransactionDocument
import br.com.ronistone.stonebank.query.repository.TransactionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.UUID

class TransactionServiceImplTest {

    @Test
    fun `getExtract repository return null`() {
        val transactionRepository = Mockito.mock(TransactionRepository::class.java)
        val transactionServiceImpl = getTransactionServiceImpl(transactionRepository)

        Mockito.`when`(transactionRepository.findByReceiverOrPayer(any(UUID::class.java)))
                .thenReturn(null)

        val transactions = transactionServiceImpl.getExtract(UUID.randomUUID())

        assertThat(transactions)
                .isNull()
    }

    @Test
    fun `getExtract repository return transactions`() {
        val transactionRepository = Mockito.mock(TransactionRepository::class.java)
        val transactionServiceImpl = getTransactionServiceImpl(transactionRepository)

        Mockito.`when`(transactionRepository.findByReceiverOrPayer(any(UUID::class.java)))
                .thenReturn(listOf(TransactionDocument(), TransactionDocument()))

        val transactions = transactionServiceImpl.getExtract(UUID.randomUUID())

        assertThat(transactions)
                .isNotNull
                .hasSize(2)
    }

    private fun getTransactionServiceImpl(transactionRepository: TransactionRepository): TransactionServiceImpl {
        return TransactionServiceImpl(transactionRepository)
    }

}