package br.com.ronistone.stonebank.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity(name = "customer")
@Table(name = "CUSTOMER")
data class CustomerEntity(
        @Id
        @GeneratedValue( strategy = GenerationType.AUTO )
        @Column(name="ID")
        val id: UUID? = null,

        @Column(name = "NAME")
        var name: String? = null,

        @Column(name = "DOCUMENT")
        var document: String? = null
) : AuditedEntity()