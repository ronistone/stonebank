package br.com.ronistone.stonebank.domain

import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@EntityListeners(AccountListener::class)
@Entity(name = "account")
@Table(name = "ACCOUNT")
class Account(
        @Id
        @GeneratedValue( strategy = GenerationType.AUTO )
        @Column(name="ID")
        val id: UUID? = null,

        @Column(name="AMOUNT")
        var amount: BigDecimal? = null,

        @OneToOne
        @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
        val customer: Customer? = null,

        @Column(name = "STATUS")
        @Enumerated(EnumType.STRING)
        var status: AccountStatus? = null,

        createdAt: Date? = null,
        updatedAt: Date? = null
) : AuditedEntity(createdAt, updatedAt)