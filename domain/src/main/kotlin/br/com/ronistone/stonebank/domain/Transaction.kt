package br.com.ronistone.stonebank.domain

import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity(name = "transaction")
@Table(name = "TRANSACTION")
data class Transaction(

        @Id
        @GeneratedValue( strategy = GenerationType.AUTO )
        @Column(name="ID")
        val id: UUID? = null,

        @Column(name = "TYPE")
        @Enumerated(EnumType.STRING)
        val type: TransactionType? = null,

        @Column(name = "AMOUNT")
        val amount: BigDecimal? = null,

        @ManyToOne
        @JoinColumn(name = "RECEIVER", referencedColumnName = "ID")
        val receiver: Account? = null,

        @ManyToOne
        @JoinColumn(name = "PAYER", referencedColumnName = "ID")
        val payer: Account? = null

): AuditedEntity()
