package br.com.ronistone.stonebank.query.service.impl

import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.commons.ValidationException
import br.com.ronistone.stonebank.query.model.TransactionDocument
import br.com.ronistone.stonebank.query.repository.TransactionRepository
import br.com.ronistone.stonebank.query.service.TransactionService
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionServiceImpl(
        val transactionRepository: TransactionRepository
): TransactionService {

    val logger = LogManager.getLogger(TransactionService::class.java)

    override fun createOrUpdate(transaction: TransactionDocument) {
        transactionRepository.save(transaction)
    }

    override fun deleteTransaction(transaction: TransactionDocument) {
        if(transaction.id == null) {
            logger.warn("Trying delete transaction without id!!!")
            return
        }
        transactionRepository.deleteById(transaction.id)
    }

    override fun getExtract(accountId: UUID?): List<TransactionDocument> {
        if(accountId == null) {
            throw ValidationException(Error.ACCOUNT_ID_INVALID)
        }
        return transactionRepository.findByReceiverOrPayer(accountId)
    }

    override fun getTransaction(id: UUID?): TransactionDocument? {
        if(id == null) {
            throw ValidationException(Error.TRANSACTION_ID_INVALID)
        }

        val transaction = transactionRepository.findById(id)

        return if(transaction.isPresent) transaction.get() else null
    }

}