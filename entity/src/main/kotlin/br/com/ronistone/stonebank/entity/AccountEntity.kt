package br.com.ronistone.stonebank.entity

import br.com.ronistone.stonebank.domain.AccountStatus
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@EntityListeners(AccountListerner::class)
@Entity(name = "account")
@Table(name = "ACCOUNT")
class AccountEntity(
        @Id
        @GeneratedValue( strategy = GenerationType.AUTO )
        @Column(name="ID")
        val id: UUID? = null,

        @Column(name="AMOUNT")
        var amount: BigDecimal? = null,

        @OneToOne
        @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
        val customer: CustomerEntity? = null,

        @Column(name = "STATUS")
        @Enumerated(EnumType.STRING)
        var status: AccountStatus? = null,

        createdAt: Date? = null,
        updatedAt: Date? = null
) : AuditedEntity(createdAt, updatedAt)