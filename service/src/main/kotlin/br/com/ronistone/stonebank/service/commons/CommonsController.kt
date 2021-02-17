package br.com.ronistone.stonebank.service.commons

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class CommonsController(
        val stonebankProperties: StonebankProperties
) {

    @GetMapping
    fun getTest(): ResponseEntity<String> {
        return ResponseEntity.ok(stonebankProperties.test)
    }

}