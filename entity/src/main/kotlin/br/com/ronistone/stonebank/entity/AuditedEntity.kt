package br.com.ronistone.stonebank.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class AuditedEntity(
        createdAt: Date? = null,
        updateAt: Date? = null,
){

    @CreatedDate
    @Column(name = "CREATED_AT")
    val createdAt: Date? = createdAt ?: Date()

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    var updatedAt: Date? = updateAt ?: Date()
}