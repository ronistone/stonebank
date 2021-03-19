package br.com.ronistone.stonebank.query.service

import br.com.ronistone.stonebank.query.model.Account

interface AccountService {
    fun createOrUpdate(account: Account)
    fun deleteTransaction(account: Account)
}