package br.com.ronistone.stonebank.domain

import java.math.BigDecimal
import java.util.*

class TransactionDTO(
    val id: UUID? = null,
    val type: TransactionType? = null,
    val amount: BigDecimal? = null,
    val receiver: AccountDTO? = null,
    val payer: AccountDTO? = null,
    val status: TransactionStatus? = null,
    createdAt: Date? = null,
    updatedAt: Date? = null
): EventDTO(createdAt, updatedAt)