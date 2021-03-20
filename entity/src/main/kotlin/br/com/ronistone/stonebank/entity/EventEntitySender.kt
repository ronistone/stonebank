package br.com.ronistone.stonebank.entity

import br.com.ronistone.stonebank.domain.Event

interface EventEntitySender {

    fun sendEvent(event: Event)

}