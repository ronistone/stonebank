package br.com.ronistone.stonebank.domain

data class BlockListDTO(
        val document: String? = null,
        var status: BlockListStatus? = null
)