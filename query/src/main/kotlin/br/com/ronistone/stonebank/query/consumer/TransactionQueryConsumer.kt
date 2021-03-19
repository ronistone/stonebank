package br.com.ronistone.stonebank.query.consumer

import br.com.ronistone.stonebank.domain.Event
import br.com.ronistone.stonebank.domain.EventType
import br.com.ronistone.stonebank.domain.TransactionDTO
import br.com.ronistone.stonebank.query.model.toDocument
import br.com.ronistone.stonebank.query.service.TransactionService
import org.apache.logging.log4j.LogManager
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionQueryConsumer(
        val transactionService: TransactionService
) {

    val logger = LogManager.getLogger(TransactionQueryConsumer::class.java)

    @KafkaListener(topics = ["\${stonebank.kafka.query.transaction.topic}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun listen(event: Event) {
        logger.info("Received {}", event)
        val transactionDTO = event.transactionDTO as TransactionDTO

        if(event.type == EventType.UPDATE || event.type == EventType.CREATE) {
            transactionService.createOrUpdate(transactionDTO.toDocument())
        } else if (event.type == EventType.DELETE) {
            transactionService.deleteTransaction(transactionDTO.toDocument())
        }
    }

}