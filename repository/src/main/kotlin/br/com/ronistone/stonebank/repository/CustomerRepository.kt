package br.com.ronistone.stonebank.repository

import br.com.ronistone.stonebank.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<Customer, UUID> {
    fun findByDocument(document: String): Customer?
}