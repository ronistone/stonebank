package br.com.ronistone.stonebank.query.repository

import br.com.ronistone.stonebank.query.model.Account
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.UUID

interface AccountRepository : ElasticsearchRepository<Account, UUID> {
}