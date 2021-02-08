package br.com.ronistone.stonebank.domain

import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity(name = "account")
@Table(name = "ACCOUNT")
data class Account(
        @Id
        @GeneratedValue( strategy = GenerationType.AUTO )
        @Column(name="ID")
        val id: UUID?,

        @Column(name="AMOUNT")
        var amount: BigDecimal? = null,

        @OneToOne
        @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
        val customer: Customer? = null
) : AuditedEntity()