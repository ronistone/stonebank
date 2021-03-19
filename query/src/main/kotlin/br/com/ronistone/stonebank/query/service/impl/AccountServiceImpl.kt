package br.com.ronistone.stonebank.query.service.impl

import br.com.ronistone.stonebank.query.model.Account
import br.com.ronistone.stonebank.query.model.Transaction
import br.com.ronistone.stonebank.query.repository.AccountRepository
import br.com.ronistone.stonebank.query.service.AccountService
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
        val accountRepository: AccountRepository
): AccountService {

    val logger = LogManager.getLogger(AccountService::class.java)

    override fun createOrUpdate(account: Account) {
        accountRepository.save(account)
    }

    override fun deleteTransaction(account: Account) {
        if(account.id == null) {
            logger.warn("Trying delete account without id!!!")
            return
        }
        accountRepository.deleteById(account.id)
    }
}