package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionDTO
import br.com.ronistone.stonebank.domain.TransactionStatus
import br.com.ronistone.stonebank.domain.TransactionType
import br.com.ronistone.stonebank.repository.TransactionRepository
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.service.commons.Error
import br.com.ronistone.stonebank.service.commons.ValidationException
import br.com.ronistone.stonebank.service.commons.copyWithExample
import br.com.ronistone.stonebank.service.commons.isGreaterThan
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.Optional
import java.util.UUID
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Service
class TransactionServiceImpl(
    val transactionRepository: TransactionRepository,
    val kafkaTemplate: KafkaTemplate<JvmType.Object, Transaction>,
    @Value("\${stonebank.kafka.topic.transaction:}") val transactionTopicName: String
) : TransactionService {

    override fun getExtract(account: Account) : List<Transaction>? {
        return account.id?.let { transactionRepository.findByReceiverOrPayer(it) }
    }

    @Transactional
    override fun deposit(account: Account, transaction: Transaction) : Transaction {
        return createTransaction(transaction, account, TransactionType.DEPOSIT, Error.DEPOSIT_NOT_POSITIVE)
    }

    @Transactional
    override fun withdraw(account: Account, transaction: Transaction) : Transaction {
        return createTransaction(transaction, account, TransactionType.WITHDRAW, Error.WITHDRAW_NOT_POSITIVE)
    }

    @Transactional
    override fun transfer(account: Account, transaction: Transaction): Transaction {
        return createTransaction(
            transaction.copyWithExample(Transaction(status = TransactionStatus.COMPLETED)),
            account,
            TransactionType.TRANSFER, Error.TRANSFER_NOT_POSITIVE
        )
    }

    @Transactional
    override fun createTransfer(accountId: UUID, transactionDTO: TransactionDTO): Transaction {
        var transaction = Transaction(
            type = TransactionType.TRANSFER,
            amount = transactionDTO.amount,
            receiver = Account(transactionDTO.receiver?.id),
            payer = Account(accountId),
            status = TransactionStatus.CREATED
        )

        transaction = transactionRepository.save(transaction)

        kafkaTemplate.send(transactionTopicName, transaction)

        return transaction
    }

    override fun getTransaction(transactionId: UUID): Optional<Transaction> {
        return transactionRepository.findById(transactionId)
    }

    private fun createTransaction(transaction: Transaction, account: Account, type: TransactionType, error: Error): Transaction {
        if (transaction.amount!!.isGreaterThan(BigDecimal.ZERO)) {
            account.let {
                return transactionRepository.save(Transaction(
                    id = transaction.id,
                    type = type,
                    receiver = transaction.receiver,
                    payer = transaction.payer,
                    amount = transaction.amount,
                    status = transaction.status ?: TransactionStatus.COMPLETED
                ))
            }
        }
        throw ValidationException(error)
    }
}
