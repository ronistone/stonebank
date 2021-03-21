package br.com.ronistone.stonebank.query.model

import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionStatus
import br.com.ronistone.stonebank.domain.TransactionType
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

@Document(indexName = "transaction")
class TransactionDocument(

        @Id
        val id: UUID? = null,

        @Field(type = FieldType.Keyword)
        val type: TransactionType? = null,

        @Field(type = FieldType.Keyword)
        val status: TransactionStatus? = null,

        val amount: BigDecimal? = null,
        val receiver: AccountDocument? = null,
        val payer: AccountDocument? = null,
        val createdAt: Date? = null,
        val updatedAt: Date? = null
)

fun Transaction.toDocument() = TransactionDocument(
        id = this.id,
        type = this.type,
        status = this.status,
        amount = this.amount,
        receiver = this.receiver?.toDocument(),
        payer = this.payer?.toDocument(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)

fun TransactionDocument.toDomain() = Transaction(
        id = this.id,
        type = this.type,
        status = this.status,
        amount = this.amount,
        receiver = this.receiver?.toDomain(),
        payer = this.payer?.toDomain(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)