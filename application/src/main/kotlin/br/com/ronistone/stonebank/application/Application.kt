package br.com.ronistone.stonebank.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication


@SpringBootApplication
@EnableJpaRepositories(basePackages = ["br.com.ronistone.stonebank.repository"])
@ComponentScan(basePackages = ["br.com.ronistone.stonebank", "br.com.ronistone.stonebank.repository"])
@EntityScan(basePackages = ["br.com.ronistone.stonebank"])
class Application: SpringBootProcessApplication()

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}