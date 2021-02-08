package br.com.ronistone.stonebank.service.impl

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.domain.TransactionType
import br.com.ronistone.stonebank.repository.TransactionRepository
import br.com.ronistone.stonebank.service.TransactionService
import br.com.ronistone.stonebank.service.commons.Error
import br.com.ronistone.stonebank.service.commons.ValidationException
import br.com.ronistone.stonebank.service.commons.isGreaterThan
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class TransactionServiceImpl(
    val transactionRepository: TransactionRepository
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
        return createTransaction(transaction, account, TransactionType.TRANSFER, Error.TRANSFER_NOT_POSITIVE)
    }

    private fun createTransaction(transaction: Transaction, account: Account, type: TransactionType, error: Error): Transaction {
        if (transaction.amount!!.isGreaterThan(BigDecimal.ZERO)) {
            account.let {
                return transactionRepository.save(Transaction(
                    type = type,
                    receiver = transaction.receiver,
                    payer = transaction.payer,
                    amount = transaction.amount
                ))
            }
        }
        throw ValidationException(error)
    }
}
