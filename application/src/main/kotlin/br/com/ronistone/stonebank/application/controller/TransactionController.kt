package br.com.ronistone.stonebank.application.controller

import br.com.ronistone.stonebank.domain.TransactionDTO
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.service.commons.toDTO
import br.com.ronistone.stonebank.service.commons.toEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(path = ["/api/transaction"])
class TransactionController(
    val transactionService: TransactionService
) {

    @PostMapping(path = ["/customer/{id}/transfer"])
    fun transfer(@PathVariable id: UUID, @RequestBody transactionDTO: TransactionDTO): TransactionDTO {
        return transactionService.createTransfer(id, transactionDTO.toEntity()).toDTO()
    }

    @GetMapping(path = ["/{id}"])
    fun findTransaction(@PathVariable("id") id: UUID): ResponseEntity<TransactionDTO> {
        val transaction = transactionService.getTransaction(id)

        if(transaction.isEmpty) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(transaction.get().toDTO())
    }

}