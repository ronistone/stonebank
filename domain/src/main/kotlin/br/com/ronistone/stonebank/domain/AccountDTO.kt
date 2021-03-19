package br.com.ronistone.stonebank.domain

import java.math.BigDecimal
import java.util.*

class AccountDTO(
    val id: UUID? = null,
    val name: String? = null,
    val document: String? = null,
    val amount: BigDecimal? = null,
    val status: AccountStatus? = null,
    createdAt: Date? = null,
    updatedAt: Date? = null
) : EventDTO(createdAt, updatedAt)