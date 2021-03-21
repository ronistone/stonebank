package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.entity.AccountEntity
import br.com.ronistone.stonebank.entity.TransactionEntity
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

interface TransactionService {

    @Transactional
    fun deposit(accountEntity: AccountEntity, transactionEntity: TransactionEntity): TransactionEntity

    @Transactional
    fun withdraw(accountEntity: AccountEntity, transactionEntity: TransactionEntity): TransactionEntity

    @Transactional
    fun transfer(accountEntity: AccountEntity, transactionEntity: TransactionEntity): TransactionEntity
    @Transactional
    fun createTransfer(accountId: UUID, transactionEntityTransfer: TransactionEntity): TransactionEntity
    fun getTransaction(transactionId: UUID): Optional<TransactionEntity>
}