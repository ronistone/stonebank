package br.com.ronistone.stonebank.query.repository

import br.com.ronistone.stonebank.query.model.AccountDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.UUID

interface AccountRepository : ElasticsearchRepository<AccountDocument, UUID> {

    fun findByCustomer_Document(document: String): AccountDocument?


}