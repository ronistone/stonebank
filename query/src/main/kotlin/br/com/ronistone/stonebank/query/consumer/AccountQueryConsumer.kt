package br.com.ronistone.stonebank.query.consumer

import br.com.ronistone.stonebank.domain.Account
import br.com.ronistone.stonebank.domain.Event
import br.com.ronistone.stonebank.domain.EventType
import br.com.ronistone.stonebank.query.model.toDocument
import br.com.ronistone.stonebank.query.service.AccountService
import org.apache.logging.log4j.LogManager
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class AccountQueryConsumer(
        val accountService: AccountService
) {


    val logger = LogManager.getLogger(AccountQueryConsumer::class.java)
    @KafkaListener(topics = ["\${stonebank.kafka.query.account.topic}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun listen(event: Event) {
        logger.info("Received {}", event)
        val accountDTO = event.account as Account

        if(event.type == EventType.UPDATE || event.type == EventType.CREATE) {
            accountService.createOrUpdate(accountDTO.toDocument())
        } else if (event.type == EventType.DELETE) {
            accountService.deleteTransaction(accountDTO.toDocument())
        }
    }

}