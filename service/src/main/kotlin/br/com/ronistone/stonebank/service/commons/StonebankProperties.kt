package br.com.ronistone.stonebank.service.commons

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("stonebank")
@RefreshScope
class StonebankProperties {

    lateinit var test: String

}