package br.com.ronistone.stonebank.query.service.impl

import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.commons.ValidationException
import br.com.ronistone.stonebank.query.model.AccountDocument
import br.com.ronistone.stonebank.query.repository.AccountRepository
import br.com.ronistone.stonebank.query.service.AccountService
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccountServiceImpl(
        val accountRepository: AccountRepository
): AccountService {

    val logger = LogManager.getLogger(AccountService::class.java)

    override fun createOrUpdate(account: AccountDocument): AccountDocument {
        return accountRepository.save(account)
    }

    override fun deleteAccount(account: AccountDocument) {
        if(account.id == null) {
            logger.warn("Trying delete account without id!!!")
            return
        }
        accountRepository.deleteById(account.id)
    }

    override fun getAccountByDocument(document: String?): AccountDocument? {
        if(document == null) {
            throw ValidationException(Error.DOCUMENT_INVALID)
        }
        return accountRepository.findByCustomer_Document(document)
    }

    override fun getBalance(accountId: UUID?): AccountDocument {
        if(accountId == null) {
            throw ValidationException(Error.ACCOUNT_ID_INVALID)
        }
        val account = accountRepository.findById(accountId)

        if(account.isPresent) {
            return account.get().let {
                AccountDocument(
                        id = it.id,
                        amount = it.amount
                )
            }
        }

        throw ValidationException(Error.ACCOUNT_NOT_FOUND)
    }
}