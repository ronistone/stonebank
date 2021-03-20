package br.com.ronistone.stonebank.entity

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.BlockList
import br.com.ronistone.stonebank.domain.Transaction

fun TransactionEntity.toDTO() = Transaction(
        id = this.id,
        type = this.type,
        amount = this.amount,
        receiver = if (this.receiver?.id != null) Account(id = this.receiver?.id) else null,
        payer = if (this.payer?.id != null) Account(id = this.payer?.id) else null,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)

fun AccountEntity.toDTO() = Account(
        id = this.id,
        name = this.customer?.name,
        document = this.customer?.document,
        amount = this.amount,
        status = this.status,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt,
)

fun BlockListEntity.toDTO() = BlockList(
        document = this.document,
        status = this.status
)

fun Transaction.toEntity() = TransactionEntity(
        id = this.id,
        type = this.type,
        amount = this.amount,
        receiver = if (this.receiver?.id != null) AccountEntity(id = this.receiver?.id) else null,
        payer = if (this.payer?.id != null) AccountEntity(id = this.payer?.id) else null,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)

fun Transaction.copyWithExample(example: Transaction) = Transaction(
        id = example.id ?: this.id,
        type = example.type ?: this.type,
        amount = example.amount ?: this.amount,
        receiver = example.receiver ?: this.receiver,
        payer = example.payer ?: this.payer,
        status = example.status ?: this.status,
        createdAt = example.createdAt ?: this.createdAt,
        updatedAt = example.updatedAt ?: this.updatedAt
)
fun TransactionEntity.copyWithExample(example: TransactionEntity) = TransactionEntity(
        id = example.id ?: this.id,
        type = example.type ?: this.type,
        amount = example.amount ?: this.amount,
        receiver = example.receiver ?: this.receiver,
        payer = example.payer ?: this.payer,
        status = example.status ?: this.status,
        createdAt = example.createdAt ?: this.createdAt,
        updatedAt = example.updatedAt ?: this.updatedAt,
)

fun Account.toEntity() = AccountEntity(
        id = this.id,
        customer = CustomerEntity(
                name = this.name,
                document = this.document,
        ),
        amount = this.amount,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
)
