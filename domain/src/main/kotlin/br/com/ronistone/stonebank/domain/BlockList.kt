package br.com.ronistone.stonebank.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "blockList")
@Table(name = "BLOCK_LIST")
data class BlockList(
        @Id
        @Column(name="DOCUMENT")
        val document: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "STATUS")
        var status: BlockListStatus? = null
) : AuditedEntity()