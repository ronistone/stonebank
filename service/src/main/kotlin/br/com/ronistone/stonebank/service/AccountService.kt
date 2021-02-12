package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionDTO
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface AccountService {
    fun createAccount(account: Account): Account
    fun getBalance(accountId: UUID): Account
    fun deposit(accountId: UUID, transactionDeposit: Transaction): Account
    fun getExtract(accountId: UUID): List<Transaction>
    @Transactional
    fun withdraw(accountId: UUID, transactionWithdraw: Transaction): Account
    @Transactional
    fun transfer(transactionTransfer: Transaction): Account
    fun getAccountByDocument(document: String?): Account
}