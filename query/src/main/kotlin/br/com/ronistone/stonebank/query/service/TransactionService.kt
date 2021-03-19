package br.com.ronistone.stonebank.query.service

import br.com.ronistone.stonebank.query.model.Transaction

interface TransactionService {
    fun createOrUpdate(transaction: Transaction)
    fun deleteTransaction(transaction: Transaction)
}