package br.com.ronistone.stonebank.repository

import br.com.ronistone.stonebank.entity.BlockListEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BlockListRepository: JpaRepository<BlockListEntity, String>