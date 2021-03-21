package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.commons.ValidationException
import br.com.ronistone.stonebank.entity.AccountEntity
import br.com.ronistone.stonebank.entity.TransactionEntity
import br.com.ronistone.stonebank.domain.TransactionStatus
import br.com.ronistone.stonebank.domain.TransactionType
import br.com.ronistone.stonebank.repository.TransactionRepository
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.commons.Error
import br.com.ronistone.stonebank.entity.copyWithExample
import br.com.ronistone.stonebank.domain.isGreaterThan
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.Optional
import java.util.UUID

@Service
class TransactionServiceImpl(
        val transactionRepository: TransactionRepository,
        val kafkaTemplate: KafkaTemplate<Any, TransactionEntity>,
        @Value("\${stonebank.kafka.topic.transaction:}") val transactionTopicName: String
) : TransactionService {

    @Transactional
    override fun deposit(accountEntity: AccountEntity, transactionEntity: TransactionEntity) : TransactionEntity {
        return createTransaction(transactionEntity, accountEntity, TransactionType.DEPOSIT, Error.DEPOSIT_NOT_POSITIVE)
    }

    @Transactional
    override fun withdraw(accountEntity: AccountEntity, transactionEntity: TransactionEntity) : TransactionEntity {
        return createTransaction(transactionEntity, accountEntity, TransactionType.WITHDRAW, Error.WITHDRAW_NOT_POSITIVE)
    }

    @Transactional
    override fun transfer(accountEntity: AccountEntity, transactionEntity: TransactionEntity): TransactionEntity {
        return createTransaction(
            transactionEntity.copyWithExample(TransactionEntity(status = TransactionStatus.COMPLETED)),
            accountEntity,
            TransactionType.TRANSFER, Error.TRANSFER_NOT_POSITIVE
        )
    }

    @Transactional
    override fun createTransfer(accountId: UUID, transactionEntityTransfer: TransactionEntity): TransactionEntity {
        var transaction = TransactionEntity(
            type = TransactionType.TRANSFER,
            amount = transactionEntityTransfer.amount,
            receiver = AccountEntity(transactionEntityTransfer.receiver?.id),
            payer = AccountEntity(accountId),
            status = TransactionStatus.CREATED
        )

        transaction = transactionRepository.save(transaction)

        kafkaTemplate.send(transactionTopicName, transaction)

        return transaction
    }

    override fun getTransaction(transactionId: UUID): Optional<TransactionEntity> {
        return transactionRepository.findById(transactionId)
    }

    private fun createTransaction(transactionEntity: TransactionEntity, accountEntity: AccountEntity, type: TransactionType, error: Error): TransactionEntity {
        if (transactionEntity.amount!!.isGreaterThan(BigDecimal.ZERO)) {
            accountEntity.let {
                return transactionRepository.save(TransactionEntity(
                    id = transactionEntity.id,
                    type = type,
                    receiver = transactionEntity.receiver,
                    payer = transactionEntity.payer,
                    amount = transactionEntity.amount,
                    status = transactionEntity.status ?: TransactionStatus.COMPLETED
                ))
            }
        }
        throw ValidationException(error)
    }
}
