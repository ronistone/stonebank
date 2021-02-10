package br.com.ronistone.stonebank.domain

import java.math.BigDecimal
import java.util.*

data class TransactionDTO(
    val id: UUID? = null,
    val type: TransactionType? = null,
    val amount: BigDecimal? = null,
    val receiver: AccountDTO? = null,
    val payer: AccountDTO? = null,
    val createdAt: Date? = null,
    val status: TransactionStatus? = null
)