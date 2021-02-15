package br.com.ronistone.stonebank.repository

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository: JpaRepository<Account, UUID> {
    fun findByCustomerDocument(document: String): Account?

    @Query("SELECT a.* FROM account a WHERE a.id = ?1 AND a.STATUS = 'CREATED'", nativeQuery = true)
    override fun findById(id: UUID): Optional<Account>

    fun findAccountById(id: UUID): Optional<Account>
}