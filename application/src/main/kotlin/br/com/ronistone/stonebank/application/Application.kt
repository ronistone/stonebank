package br.com.ronistone.stonebank.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@SpringBootApplication
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = ["br.com.ronistone.stonebank.repository"])
@ComponentScan(basePackages = ["br.com.ronistone.stonebank", "br.com.ronistone.stonebank.repository"])
@EntityScan(basePackages = ["br.com.ronistone.stonebank"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}