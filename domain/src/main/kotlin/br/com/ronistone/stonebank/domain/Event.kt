package br.com.ronistone.stonebank.domain

data class Event(
        val type: EventType,
        val transaction: Transaction? = null,
        val account: Account? = null
) {

    var entity: EventDTO = (transaction ?: account) as EventDTO

    override fun toString(): String {
        return "Event(type=$type, entity=$entity)"
    }
}