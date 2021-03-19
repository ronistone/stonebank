package br.com.ronistone.stonebank.domain

import br.com.ronistone.stonebank.service.commons.toDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import javax.persistence.PostPersist
import javax.persistence.PostRemove
import javax.persistence.PostUpdate

class AccountListener(
        val kafkaTemplate: KafkaTemplate<Any, Event>,
        @Value("\${stonebank.kafka.query.account.topic}") val accountQueryTopic: String
) {

    @PostPersist
    fun afterCreate(account: Account){
        sendEvent(Event(
            type = EventType.CREATE,
            accountDTO = account.toDTO(),
        ))
    }

    @PostRemove
    fun afterDelete(account: Account){
        sendEvent(Event(
                type = EventType.DELETE,
                accountDTO = account.toDTO(),
        ))
    }

    @PostUpdate
    fun afterUpdate(account: Account){
        sendEvent(Event(
                type = EventType.UPDATE,
                accountDTO = account.toDTO(),
        ))
    }
    private fun sendEvent(event: Event) {
        kafkaTemplate.send(
                accountQueryTopic,
                event
        )
    }
}
