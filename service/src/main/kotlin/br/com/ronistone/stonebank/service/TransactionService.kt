package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Transaction
import org.springframework.transaction.annotation.Transactional

interface TransactionService {
    fun getExtract(account: Account): List<Transaction>?

    @Transactional
    fun deposit(account: Account, transaction: Transaction): Transaction

    @Transactional
    fun withdraw(account: Account, transaction: Transaction): Transaction

    @Transactional
    fun transfer(account: Account, transaction: Transaction): Transaction
}