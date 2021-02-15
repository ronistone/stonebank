package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.domain.BlockList
import br.com.ronistone.stonebank.domain.BlockListStatus
import br.com.ronistone.stonebank.repository.BlockListRepository
import br.com.ronistone.stonebank.service.BlockListService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class BlockListServiceImpl(
        val blockListRepository: BlockListRepository
): BlockListService {


    override fun isBlocked(document: String): Boolean {

        val documentBlockList = blockListRepository.findById(document)

        if(documentBlockList.isPresent) {
            return documentBlockList.get().status == BlockListStatus.BLOCKED
        }

        return false
    }

    @Transactional
    override fun blockDocument(document: String): BlockList {

        val documentBlockList = blockListRepository.findById(document)

        if(documentBlockList.isPresent) {
            return blockListRepository.save(documentBlockList.get().apply {
                status = BlockListStatus.BLOCKED
            })
        }

        return blockListRepository.save(
                BlockList(
                        document = document,
                        status = BlockListStatus.BLOCKED
                )
        )
    }

    @Transactional
    override fun releaseDocument(document: String): BlockList {

        val documentBlockList = blockListRepository.findById(document)

        if(documentBlockList.isPresent) {
            return blockListRepository.save(documentBlockList.get().apply {
                status = BlockListStatus.RELEASED
            })
        }

        return blockListRepository.save(
                BlockList(
                        document = document,
                        status = BlockListStatus.RELEASED
                )
        )
    }


}