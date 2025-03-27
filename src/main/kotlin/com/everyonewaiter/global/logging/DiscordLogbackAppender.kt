package com.everyonewaiter.global.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import ch.qos.logback.core.UnsynchronizedAppenderBase
import com.everyonewaiter.common.notification.discord.DiscordEmbed
import com.everyonewaiter.common.notification.discord.DiscordField
import com.everyonewaiter.common.notification.discord.DiscordWebhookPayload
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val COLOR_RED = 16711680

private val logger = KotlinLogging.logger {}

class DiscordLogbackAppender : UnsynchronizedAppenderBase<ILoggingEvent>() {
    private val discordWebhookClient = RestClient.create("https://discord.com/api/webhooks")

    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var discordWebhookUri: String

    override fun append(loggingEvent: ILoggingEvent) {
        val payload = createPayload(loggingEvent)
        discordWebhookClient
            .post()
            .uri("/$discordWebhookUri")
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleError)
            .onStatus(HttpStatusCode::is5xxServerError, this::handleError)
            .toBodilessEntity()
    }

    private fun createPayload(loggingEvent: ILoggingEvent): DiscordWebhookPayload {
        val embeds = mutableListOf<DiscordEmbed>()
        val mdc: Map<String, String> = loggingEvent.mdcPropertyMap
        val throwable = ThrowableProxyUtil.asString(loggingEvent.throwableProxy)
        val stackTrace = if (throwable.length > 700) throwable.substring(0, 700) else throwable

        embeds.add(
            DiscordEmbed(
                title = "Error Information",
                description = loggingEvent.formattedMessage,
                color = COLOR_RED,
                fields = listOf(
                    DiscordField(
                        name = "Timestamp",
                        value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    ),
                    DiscordField(
                        name = "Request ID",
                        value = mdc["requestId"] ?: "",
                    ),
                    DiscordField(
                        name = "Request URI",
                        value = mdc["requestUri"] ?: "",
                    ),
                    DiscordField(
                        name = "Request Parameters",
                        value = mdc["requestParameters"] ?: "",
                    ),
                    DiscordField(
                        name = "Request Headers",
                        value = mdc["requestHeaders"] ?: "",
                    ),
                    DiscordField(
                        name = "Request Cookies",
                        value = mdc["requestCookies"] ?: "",
                    ),
                ),
            ),
        )
        embeds.add(
            DiscordEmbed(
                title = "Stack Trace",
                description = "```kotlin\n$stackTrace\n```",
                color = COLOR_RED,
                fields = listOf(),
            ),
        )

        return DiscordWebhookPayload(embeds)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun handleError(
        request: HttpRequest,
        response: ClientHttpResponse,
    ) {
        logger.warn { "[디스코드 메시지 전송 실패] ${response.statusText} : ${String(response.body.readAllBytes())}" }
    }
}
