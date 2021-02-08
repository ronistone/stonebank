package br.com.ronistone.stonebank.repository

import br.com.ronistone.stonebank.domain.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<Transaction, UUID> {

    @Query( "select t.* from transaction t where t.payer = ?1 or t.receiver = ?1", nativeQuery = true )
    fun findByReceiverOrPayer(accountId: UUID) : List<Transaction>
}