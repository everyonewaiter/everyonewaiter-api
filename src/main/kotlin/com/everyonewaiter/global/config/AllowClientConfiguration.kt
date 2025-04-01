package com.everyonewaiter.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AllowClientConfiguration(
    @Value("\${allow.client.urls}") val urls: List<String>,
) {
    val baseUrl: String
        get() = urls.first()
}
