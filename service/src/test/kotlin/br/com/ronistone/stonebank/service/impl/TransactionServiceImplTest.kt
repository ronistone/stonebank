package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.repository.TransactionRepository
import br.com.ronistone.stonebank.service.Utils.Companion.any
import br.com.ronistone.stonebank.service.commons.Error
import br.com.ronistone.stonebank.service.commons.ValidationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.math.BigDecimal
import java.util.UUID

class TransactionServiceImplTest {
//
//    @Test
//    fun `getExtract repository return null`() {
//        val transactionRepository = mock(TransactionRepository::class.java)
//        val transactionServiceImpl = TransactionServiceImpl(transactionRepository)
//
//        `when`(transactionRepository.findByReceiverOrPayer(any(UUID::class.java)))
//            .thenReturn(null)
//
//        val transactions = transactionServiceImpl.getExtract(Account(id = UUID.randomUUID()))
//
//        assertThat(transactions)
//            .isNull()
//    }
//
//    @Test
//    fun `getExtract repository return transactions`() {
//        val transactionRepository = mock(TransactionRepository::class.java)
//        val transactionServiceImpl = TransactionServiceImpl(transactionRepository)
//
//        `when`(transactionRepository.findByReceiverOrPayer(any(UUID::class.java)))
//            .thenReturn(listOf(Transaction(), Transaction()))
//
//        val transactions = transactionServiceImpl.getExtract(Account(id = UUID.randomUUID()))
//
//        assertThat(transactions)
//            .isNotNull
//            .hasSize(2)
//    }
//
//    @Test
//    fun `deposit invalid value throw exception`() {
//        val transactionServiceImpl = TransactionServiceImpl(mock(TransactionRepository::class.java))
//
//        try {
//            transactionServiceImpl.deposit(Account(id = UUID.randomUUID()), Transaction(amount = BigDecimal.ZERO))
//            fail("Expected validation exception")
//        } catch (ex: ValidationException) {
//            assertThat(ex.errors)
//                .hasSize(1)
//                .contains(Error.DEPOSIT_NOT_POSITIVE)
//        }
//    }
//
//    @Test
//    fun `withdraw invalid value throw exception`() {
//        val transactionServiceImpl = TransactionServiceImpl(mock(TransactionRepository::class.java))
//
//        try {
//            transactionServiceImpl.withdraw(Account(id = UUID.randomUUID()), Transaction(amount = BigDecimal.ZERO))
//            fail("Expected validation exception")
//        } catch (ex: ValidationException) {
//            assertThat(ex.errors)
//                .hasSize(1)
//                .contains(Error.WITHDRAW_NOT_POSITIVE)
//        }
//    }
//
//    @Test
//    fun `transfer invalid value throw exception`() {
//        val transactionServiceImpl = TransactionServiceImpl(mock(TransactionRepository::class.java))
//
//        try {
//            transactionServiceImpl.transfer(Account(id = UUID.randomUUID()), Transaction(amount = BigDecimal.ZERO))
//            fail("Expected validation exception")
//        } catch (ex: ValidationException) {
//            assertThat(ex.errors)
//                .hasSize(1)
//                .contains(Error.TRANSFER_NOT_POSITIVE)
//        }
//    }
//
//    @Test
//    fun `deposit success`() {
//        val transactionRepository = mock(TransactionRepository::class.java)
//        val transactionServiceImpl = TransactionServiceImpl(transactionRepository)
//
//        `when`(transactionRepository.save(any(Transaction::class.java)))
//            .thenReturn(Transaction())
//
//        transactionServiceImpl.deposit(Account(id = UUID.randomUUID()), Transaction(amount = BigDecimal.TEN))
//
//        verify(transactionRepository, times(1)).save(any(Transaction::class.java))
//    }
//
//    @Test
//    fun `withdraw success`() {
//        val transactionRepository = mock(TransactionRepository::class.java)
//        val transactionServiceImpl = TransactionServiceImpl(transactionRepository)
//
//        `when`(transactionRepository.save(any(Transaction::class.java)))
//            .thenReturn(Transaction())
//
//        transactionServiceImpl.withdraw(Account(id = UUID.randomUUID()), Transaction(amount = BigDecimal.TEN))
//
//        verify(transactionRepository, times(1)).save(any(Transaction::class.java))
//    }
//
//    @Test
//    fun `transfer success`() {
//        val transactionRepository = mock(TransactionRepository::class.java)
//        val transactionServiceImpl = TransactionServiceImpl(transactionRepository)
//
//        `when`(transactionRepository.save(any(Transaction::class.java)))
//            .thenReturn(Transaction())
//
//        transactionServiceImpl.transfer(Account(id = UUID.randomUUID()), Transaction(amount = BigDecimal.TEN))
//
//        verify(transactionRepository, times(1)).save(any(Transaction::class.java))
//    }

}