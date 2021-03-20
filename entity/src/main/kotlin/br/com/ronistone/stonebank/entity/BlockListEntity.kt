package br.com.ronistone.stonebank.entity

import br.com.ronistone.stonebank.domain.BlockListStatus
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "blockList")
@Table(name = "BLOCK_LIST")
data class BlockListEntity(
        @Id
        @Column(name="DOCUMENT")
        val document: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "STATUS")
        var status: BlockListStatus? = null
) : AuditedEntity()