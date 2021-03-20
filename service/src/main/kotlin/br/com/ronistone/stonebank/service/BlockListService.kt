package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.entity.BlockListEntity

interface BlockListService {
    fun isBlocked(document: String): Boolean
    fun blockDocument(document: String): BlockListEntity
    fun releaseDocument(document: String): BlockListEntity
}