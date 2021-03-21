package br.com.ronistone.stonebank.query.controller

import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.query.model.toDomain
import br.com.ronistone.stonebank.query.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping( path = ["/api/transaction"])
class TransactionQueryController(
        val transactionService: TransactionService
) {

    @GetMapping(path = ["/extract/account/{id}"])
    fun getExtract(@PathVariable("id") accountId: UUID): List<Transaction> {
        val transactions = transactionService.getExtract(accountId)

        return transactions.stream().map {
            it.toDomain()
        }.collect(Collectors.toList())
    }

    @GetMapping(path = ["/{id}"])
    fun findTransaction(@PathVariable("id") id: UUID): ResponseEntity<Transaction> {
        val transaction = transactionService.getTransaction(id) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(transaction.toDomain())
    }

}