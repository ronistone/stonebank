package br.com.ronistone.stonebank.application.controller

import br.com.ronistone.stonebank.domain.AccountDTO
import br.com.ronistone.stonebank.domain.TransactionDTO
import br.com.ronistone.stonebank.service.AccountService
import br.com.ronistone.stonebank.service.commons.toDTO
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping( path = ["/api/account"])
class AccountController(
        val accountService: AccountService
) {

    @PostMapping
    fun createAccount(@RequestBody account: AccountDTO): AccountDTO {
        return accountService.createAccount(account).toDTO()
    }

    @GetMapping
    fun getAccount(@Param("document") document: String?): AccountDTO {
        return accountService.getAccountByDocument(document).toDTO()
    }

    @GetMapping(path = [ "/{id}" ])
    fun getBalance(@PathVariable("id") accountId: UUID): AccountDTO {
        return accountService.getBalance(accountId).toDTO()
    }

    @GetMapping(path = ["/{id}/extract"])
    fun getExtract(@PathVariable("id") accountId: UUID): List<TransactionDTO> {
        val transactions = accountService.getExtract(accountId)

        return transactions.stream().map {
            it.toDTO()
        }.collect(Collectors.toList())
    }

    @PutMapping(path = ["/{id}/deposit"])
    fun deposit(@PathVariable id: UUID, @RequestBody transactionDTO: TransactionDTO): AccountDTO {
        return accountService.deposit(id, transactionDTO).toDTO()
    }

    @PutMapping(path = ["/{id}/withdraw"])
    fun withdraw(@PathVariable id: UUID, @RequestBody transactionDTO: TransactionDTO): AccountDTO {
        return accountService.withdraw(id, transactionDTO).toDTO()
    }

}