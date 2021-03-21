package br.com.ronistone.stonebank.query.service

import br.com.ronistone.stonebank.query.model.TransactionDocument
import java.util.UUID

interface TransactionService {
    fun createOrUpdate(transaction: TransactionDocument)
    fun deleteTransaction(transaction: TransactionDocument)
    fun getExtract(accountId: UUID?): List<TransactionDocument>
    fun getTransaction(id: UUID?): TransactionDocument?
}