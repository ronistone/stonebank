package br.com.ronistone.stonebank.consumer.listener

import br.com.ronistone.stonebank.domain.Transaction
import br.com.ronistone.stonebank.service.AccountService
import br.com.ronistone.stonebank.service.commons.toDTO
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class TransactionListener(
    val accountService: AccountService
) {

    @KafkaListener(topics = ["\${stonebank.kafka.transaction.topic}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun listen(transaction: Transaction){

        accountService.transfer(transactionDTO = transaction.toDTO())

    }

}