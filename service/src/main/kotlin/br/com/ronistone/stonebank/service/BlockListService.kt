package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.domain.BlockList

interface BlockListService {
    fun isBlocked(document: String): Boolean
    fun blockDocument(document: String): BlockList
    fun releaseDocument(document: String): BlockList
}