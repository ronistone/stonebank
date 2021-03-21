package br.com.ronistone.stonebank.query.service

import br.com.ronistone.stonebank.query.model.AccountDocument
import java.util.UUID

interface AccountService {
    fun createOrUpdate(account: AccountDocument) : AccountDocument
    fun deleteAccount(account: AccountDocument)
    fun getAccountByDocument(document: String?) : AccountDocument?
    fun getBalance(accountId: UUID?): AccountDocument?
}