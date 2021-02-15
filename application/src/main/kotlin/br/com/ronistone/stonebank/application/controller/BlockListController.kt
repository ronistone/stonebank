package br.com.ronistone.stonebank.application.controller

import br.com.ronistone.stonebank.domain.BlockListDTO
import br.com.ronistone.stonebank.service.BlockListService
import br.com.ronistone.stonebank.service.commons.toDTO
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/blocklist")
class BlockListController(
        val blockListService: BlockListService
) {

    @PostMapping(path = ["/{document}/block"])
    fun block(@PathVariable("document") document: String): BlockListDTO {
        return blockListService.blockDocument(document).toDTO()
    }

    @PostMapping(path = ["/{document}/release"])
    fun release(@PathVariable("document") document: String): BlockListDTO {
        return blockListService.releaseDocument(document).toDTO()
    }

}