package com.everyonewaiter.global.config

import feign.Logger
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.everyonewaiter"])
class FeignClientConfiguration {
    @Bean
    fun feignClientLoggerLevel(): Logger.Level = Logger.Level.FULL
}
