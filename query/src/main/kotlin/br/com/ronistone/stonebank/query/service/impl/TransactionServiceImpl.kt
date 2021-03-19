package br.com.ronistone.stonebank.query.service.impl

import br.com.ronistone.stonebank.query.model.Transaction
import br.com.ronistone.stonebank.query.repository.TransactionRepository
import br.com.ronistone.stonebank.query.service.TransactionService
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl(
        val transactionRepository: TransactionRepository
): TransactionService {

    val logger = LogManager.getLogger(TransactionService::class.java)

    override fun createOrUpdate(transaction: Transaction) {
        transactionRepository.save(transaction)
    }

    override fun deleteTransaction(transaction: Transaction) {
        if(transaction.id == null) {
            logger.warn("Trying delete transaction without id!!!")
            return
        }
        transactionRepository.deleteById(transaction.id)
    }

}