package br.com.ronistone.stonebank.query

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
@EnableConfigurationProperties
class QueryApplication

fun main(args: Array<String>) {
    runApplication<QueryApplication>(*args)
}
