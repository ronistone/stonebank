package br.com.ronistone.stonebank.repository

import br.com.ronistone.stonebank.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomerRepository : JpaRepository<CustomerEntity, UUID> {
    fun findByDocument(document: String): CustomerEntity?
}