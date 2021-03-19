package br.com.ronistone.stonebank.service.commons

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.AccountDTO
import br.com.ronistone.stonebank.domain.BlockList
import br.com.ronistone.stonebank.domain.BlockListDTO
import br.com.ronistone.stonebank.domain.Customer
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionDTO
import com.google.gson.Gson
import java.math.BigDecimal

fun BigDecimal.isLessThan(
    value: BigDecimal
) = this.compareTo(value) == -1

fun BigDecimal.isGreaterThan(
    value: BigDecimal
) = this.compareTo(value) == 1



fun Transaction.toDTO() = TransactionDTO(
    id = this.id,
    type = this.type,
    amount = this.amount,
    receiver = if (this.receiver?.id != null) AccountDTO(id = this.receiver?.id) else null,
    payer = if (this.payer?.id != null) AccountDTO(id = this.payer?.id) else null,
    status = this.status,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun Account.toDTO() = AccountDTO(
    id = this.id,
    name = this.customer?.name,
    document = this.customer?.document,
    amount = this.amount,
    status = this.status,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
)

fun BlockList.toDTO() = BlockListDTO(
        document = this.document,
        status = this.status
)

fun TransactionDTO.toEntity() = Transaction(
    id = this.id,
    type = this.type,
    amount = this.amount,
    receiver = if (this.receiver?.id != null) Account(id = this.receiver?.id) else null,
    payer = if (this.payer?.id != null) Account(id = this.payer?.id) else null,
    status = this.status,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun TransactionDTO.copyWithExample(example: TransactionDTO) = TransactionDTO(
    id = example.id ?: this.id,
    type = example.type ?: this.type,
    amount = example.amount ?: this.amount,
    receiver = example.receiver ?: this.receiver,
    payer = example.payer ?: this.payer,
    status = example.status ?: this.status,
    createdAt = example.createdAt ?: this.createdAt,
    updatedAt = example.updatedAt ?: this.updatedAt
)
fun Transaction.copyWithExample(example: Transaction) = Transaction(
    id = example.id ?: this.id,
    type = example.type ?: this.type,
    amount = example.amount ?: this.amount,
    receiver = example.receiver ?: this.receiver,
    payer = example.payer ?: this.payer,
    status = example.status ?: this.status,
    createdAt = example.createdAt ?: this.createdAt,
    updatedAt = example.updatedAt ?: this.updatedAt,
)

fun AccountDTO.toEntity() = Account(
    id = this.id,
    customer = Customer(
        name = this.name,
        document = this.document,
    ),
    amount = this.amount,
    status = this.status,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun <T> String.jsonToObject(t: Class<T>): T =
    Gson().fromJson(this, t)