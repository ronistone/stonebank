package br.com.ronistone.stonebank.domain

import java.math.BigDecimal
import java.util.*

class Transaction(
        val id: UUID? = null,
        val type: TransactionType? = null,
        val amount: BigDecimal? = null,
        val receiver: Account? = null,
        val payer: Account? = null,
        val status: TransactionStatus? = null,
        createdAt: Date? = null,
        updatedAt: Date? = null
): EventDTO(createdAt, updatedAt)