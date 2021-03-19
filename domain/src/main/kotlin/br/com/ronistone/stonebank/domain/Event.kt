package br.com.ronistone.stonebank.domain

data class Event(
     val type: EventType,
     val transactionDTO: TransactionDTO? = null,
     val accountDTO: AccountDTO? = null
) {

    var entity: EventDTO = (transactionDTO ?: accountDTO) as EventDTO

    override fun toString(): String {
        return "Event(type=$type, entity=$entity)"
    }
}