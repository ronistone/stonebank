package br.com.ronistone.stonebank.entity

import br.com.ronistone.stonebank.domain.Event
import br.com.ronistone.stonebank.domain.EventType
import javax.persistence.PostPersist
import javax.persistence.PostRemove
import javax.persistence.PostUpdate

class AccountListerner(val eventEntitySender: EventEntitySender) {

    @PostPersist
    fun afterCreate(accountEntity: AccountEntity){
        eventEntitySender.sendEvent(Event(
            type = EventType.CREATE,
            account = accountEntity.toDTO(),
        ))
    }

    @PostRemove
    fun afterDelete(accountEntity: AccountEntity){
        eventEntitySender.sendEvent(Event(
                type = EventType.DELETE,
                account = accountEntity.toDTO(),
        ))
    }

    @PostUpdate
    fun afterUpdate(accountEntity: AccountEntity){
        eventEntitySender.sendEvent(Event(
                type = EventType.UPDATE,
                account = accountEntity.toDTO(),
        ))
    }
}
