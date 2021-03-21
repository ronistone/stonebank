package br.com.ronistone.stonebank.query.model

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountStatus
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

@Document(indexName = "account")
class AccountDocument(
        @Id
        val id: UUID? = null,
        var amount: BigDecimal? = null,
        val customer: CustomerDocument? = null,

        @Field(type = FieldType.Keyword)
        var status: AccountStatus? = null,
        val createdAt: Date? = null,
        val updatedAt: Date? = null
)


fun Account.toDocument() = AccountDocument(
        id = this.id,
        amount = this.amount,
        customer = CustomerDocument(
                name = this.name,
                document = this.document
        ),
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)

fun AccountDocument.toDomain() = Account(
        id = this.id,
        amount = this.amount,
        name = this.customer?.name,
        document = this.customer?.document,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)