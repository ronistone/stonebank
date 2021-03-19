package br.com.ronistone.stonebank.query.repository

import br.com.ronistone.stonebank.query.model.Transaction
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.UUID

interface TransactionRepository : ElasticsearchRepository<Transaction, UUID> {
}