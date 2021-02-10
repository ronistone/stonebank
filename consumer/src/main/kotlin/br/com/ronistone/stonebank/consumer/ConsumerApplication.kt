package br.com.ronistone.stonebank.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["br.com.ronistone.stonebank.repository"])
@ComponentScan(basePackages = ["br.com.ronistone.stonebank", "br.com.ronistone.stonebank.repository"])
@EntityScan(basePackages = ["br.com.ronistone.stonebank"])
class ConsumerApplication

fun main(args: Array<String>) {
    runApplication<ConsumerApplication>(*args)
}
