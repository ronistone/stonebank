package br.com.ronistone.stonebank.repository

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository: JpaRepository<Account, UUID> {
    fun findByCustomerDocument(document: String): Account?
}