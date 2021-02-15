package br.com.ronistone.stonebank.service.task

import br.com.ronistone.stonebank.domain.AccountStatus
import br.com.ronistone.stonebank.service.AccountService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FinishingRecusedTask(
    val accountService: AccountService
): BaseTask() {

    override fun proccess(delegate: DelegateExecution) {
        val accountId = delegate.businessKey
        accountService.updateStatus(UUID.fromString(accountId), AccountStatus.RECUSED)
    }
}