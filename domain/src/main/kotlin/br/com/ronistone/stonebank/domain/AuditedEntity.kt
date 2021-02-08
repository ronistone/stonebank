package br.com.ronistone.stonebank.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class AuditedEntity {

    @CreatedDate
    @Column(name = "CREATED_AT")
    val createdAt: Date = Date()

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    var updatedAt: Date = Date()
}