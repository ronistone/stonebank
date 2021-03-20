package br.com.ronistone.stonebank.repository

import br.com.ronistone.stonebank.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface AccountRepository: JpaRepository<AccountEntity, UUID> {
    fun findByCustomerDocument(document: String): AccountEntity?

    @Query("SELECT a.* FROM account a WHERE a.id = ?1 AND a.STATUS = 'CREATED'", nativeQuery = true)
    override fun findById(id: UUID): Optional<AccountEntity>

    fun findAccountById(id: UUID): Optional<AccountEntity>
}