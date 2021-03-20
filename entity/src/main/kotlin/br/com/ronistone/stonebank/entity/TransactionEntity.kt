package br.com.ronistone.stonebank.entity

import br.com.ronistone.stonebank.domain.TransactionStatus
import br.com.ronistone.stonebank.domain.TransactionType
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@EntityListeners(TransactionListerner::class)
@Entity(name = "transaction")
@Table(name = "TRANSACTION")
class TransactionEntity(

        @Id
        @GeneratedValue( strategy = GenerationType.AUTO )
        @Column(name="ID")
        val id: UUID? = null,

        @Column(name = "TYPE")
        @Enumerated(EnumType.STRING)
        val type: TransactionType? = null,

        @Column(name= "STATUS")
        @Enumerated(EnumType.STRING)
        val status: TransactionStatus? = null,

        @Column(name = "AMOUNT")
        val amount: BigDecimal? = null,

        @ManyToOne
        @JoinColumn(name = "RECEIVER", referencedColumnName = "ID")
        val receiver: AccountEntity? = null,

        @ManyToOne
        @JoinColumn(name = "PAYER", referencedColumnName = "ID")
        val payer: AccountEntity? = null,

        createdAt: Date? = null,
        updatedAt: Date? = null

): AuditedEntity(createdAt, updatedAt) {
        override fun toString(): String {
                return "Transaction(id=$id, type=$type, status=$status, amount=$amount, receiver=$receiver, payer=$payer)"
        }
}
