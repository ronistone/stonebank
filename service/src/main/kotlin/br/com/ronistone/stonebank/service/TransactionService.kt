package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionDTO
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

interface TransactionService {
    fun getExtract(account: Account): List<Transaction>?

    @Transactional
    fun deposit(account: Account, transaction: Transaction): Transaction

    @Transactional
    fun withdraw(account: Account, transaction: Transaction): Transaction

    @Transactional
    fun transfer(account: Account, transaction: Transaction): Transaction
    @Transactional
    fun createTransfer(accountId: UUID, transactionTransfer: Transaction): Transaction
    fun getTransaction(transactionId: UUID): Optional<Transaction>
}