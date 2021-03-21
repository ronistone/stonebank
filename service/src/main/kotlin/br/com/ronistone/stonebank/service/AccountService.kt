package br.com.ronistone.stonebank.service

import br.com.ronistone.stonebank.entity.AccountEntity
import br.com.ronistone.stonebank.domain.AccountStatus
import br.com.ronistone.stonebank.entity.TransactionEntity
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface AccountService {
    fun createAccount(accountEntity: AccountEntity): AccountEntity
    fun deposit(accountId: UUID, transactionEntityDeposit: TransactionEntity): AccountEntity
    @Transactional
    fun withdraw(accountId: UUID, transactionEntityWithdraw: TransactionEntity): AccountEntity
    @Transactional
    fun transfer(transactionEntityTransfer: TransactionEntity): AccountEntity
    fun updateStatus(accountId: UUID, accountStatus: AccountStatus): AccountEntity
}