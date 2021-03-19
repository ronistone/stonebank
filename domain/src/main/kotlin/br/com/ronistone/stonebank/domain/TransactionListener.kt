package br.com.ronistone.stonebank.domain

import br.com.ronistone.stonebank.service.commons.toDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import javax.persistence.PostPersist
import javax.persistence.PostRemove
import javax.persistence.PostUpdate

//@Component
class TransactionListener(
        val kafkaTemplate: KafkaTemplate<Any, Event>,
        @Value("\${stonebank.kafka.query.transaction.topic}") val transactionQueryTopic: String
) {

    @PostPersist
    fun afterCreate(transaction: Transaction){
        sendEvent(Event(
                type = EventType.CREATE,
                transactionDTO = transaction.toDTO()
        ))
    }

    @PostRemove
    fun afterDelete(transaction: Transaction){
        sendEvent(Event(
                type = EventType.DELETE,
                transactionDTO = transaction.toDTO()
        ))
    }

    @PostUpdate
    fun afterUpdate(transaction: Transaction){
        sendEvent(Event(
                type = EventType.UPDATE,
                transactionDTO = transaction.toDTO()
        ))
    }
    private fun sendEvent(event: Event) {
        kafkaTemplate.send(
                transactionQueryTopic,
                event
        )
    }

}