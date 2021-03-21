package br.com.ronistone.stonebank.query.repository

import br.com.ronistone.stonebank.query.model.TransactionDocument
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.UUID

interface TransactionRepository : ElasticsearchRepository<TransactionDocument, UUID> {

    @Query("""
        {
            "bool": {
                "should": [
                    {"match": {"receiver.id": "?0"}},
                    {"match": {"payer.id": "?0"}}
                ]
            }
        }
    """)
    fun findByReceiverOrPayer(accountId: UUID) : List<TransactionDocument>


}