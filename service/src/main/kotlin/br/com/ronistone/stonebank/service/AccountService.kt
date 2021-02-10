package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountDTO
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionDTO
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface AccountService {
    fun createAccount(accountDTO: AccountDTO): Account
    fun getBalance(accountId: UUID): Account
    fun deposit(accountId: UUID, transactionDTO: TransactionDTO): Account
    fun getExtract(accountId: UUID): List<Transaction>
    @Transactional
    fun withdraw(accountId: UUID, transactionDTO: TransactionDTO): Account
    @Transactional
    fun transfer(transactionDTO: TransactionDTO): Account
    fun getAccountByDocument(document: String?): Account
}