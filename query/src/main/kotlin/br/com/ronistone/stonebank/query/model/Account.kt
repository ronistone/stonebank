package br.com.ronistone.stonebank.query.model

import br.com.ronistone.stonebank.domain.AccountDTO
import br.com.ronistone.stonebank.domain.AccountStatus
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

@Document(indexName = "account")
class Account(
        @Id
        val id: UUID? = null,
        var amount: BigDecimal? = null,

        @Field(type = FieldType.Nested, includeInParent = true)
        val customer: Customer? = null,

        @Field(type = FieldType.Keyword)
        var status: AccountStatus? = null,
        val createdAt: Date? = null,
        val updatedAt: Date? = null
)


fun AccountDTO.toDocument() = Account(
        id = this.id,
        amount = this.amount,
        customer = Customer(
                name = this.name,
                document = this.document
        ),
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)