package br.com.ronistone.stonebank.consumer.listener

import br.com.ronistone.stonebank.entity.TransactionEntity
import br.com.ronistone.stonebank.service.AccountService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class TransactionListener(
    val accountService: AccountService
) {

    @KafkaListener(topics = ["\${stonebank.kafka.transaction.topic}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun listen(transactionEntity: TransactionEntity){
        accountService.transfer(transactionEntity)
    }

}