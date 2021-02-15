package br.com.ronistone.stonebank.service.task

import br.com.ronistone.stonebank.service.BlockListService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.stereotype.Component

@Component
class ConsultBlockListTask(
        val blockListService: BlockListService
): BaseTask() {

    override fun proccess(delegate: DelegateExecution) {
        val document = delegate.getVariable("document")

        delegate.setVariable( "inBlockList" ,blockListService.isBlocked(document as String))
    }

}