package br.com.ronistone.stonebank.entity

import br.com.ronistone.stonebank.domain.Event
import br.com.ronistone.stonebank.domain.EventType
import javax.persistence.PostPersist
import javax.persistence.PostRemove
import javax.persistence.PostUpdate

class TransactionListerner(val eventEntitySender: EventEntitySender) {

    @PostPersist
    fun afterCreate(transactionEntity: TransactionEntity){
        eventEntitySender.sendEvent(Event(
                type = EventType.CREATE,
                transaction = transactionEntity.toDTO()
        ))
    }

    @PostRemove
    fun afterDelete(transactionEntity: TransactionEntity){
        eventEntitySender.sendEvent(Event(
                type = EventType.DELETE,
                transaction = transactionEntity.toDTO()
        ))
    }

    @PostUpdate
    fun afterUpdate(transactionEntity: TransactionEntity){
        eventEntitySender.sendEvent(Event(
                type = EventType.UPDATE,
                transaction = transactionEntity.toDTO()
        ))
    }
}