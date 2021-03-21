package br.com.ronistone.stonebank.query.controller

import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.commons.ValidationException
import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.query.model.toDomain
import br.com.ronistone.stonebank.query.service.AccountService
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping( path = ["/api/account"])
class AccountQueryController(
        val accountService: AccountService
) {

    @GetMapping
    fun getAccount(@Param("document") document: String?): Account {
        val account = accountService.getAccountByDocument(document) ?: throw ValidationException(Error.ACCOUNT_NOT_FOUND)
        return account.toDomain()
    }

    @GetMapping(path = [ "/{id}" ])
    fun getBalance(@PathVariable("id") accountId: UUID): Account {
        val account = accountService.getBalance(accountId) ?: throw ValidationException(Error.ACCOUNT_NOT_FOUND)
        return account.toDomain()
    }

}