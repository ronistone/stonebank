package br.com.ronistone.stonebank.service.listerner

import br.com.ronistone.stonebank.domain.Event
import br.com.ronistone.stonebank.entity.EventEntitySender
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class EventEntitySenderImpl(
        val kafkaTemplate: KafkaTemplate<Any, Event>,
        @Value("\${stonebank.kafka.query.account.topic}") val accountQueryTopic: String,
        @Value( "\${stonebank.kafka.query.transaction.topic}") val transationQueryTopic: String
) : EventEntitySender {

    override fun sendEvent(event: Event) {
        var topic: String? = null

        if(event.account != null) {
            topic = accountQueryTopic
        } else if(event.transaction != null) {
            topic = transationQueryTopic
        }

        topic?.let {
            kafkaTemplate.send(
                    it,
                    event
            )
        }
    }
}
