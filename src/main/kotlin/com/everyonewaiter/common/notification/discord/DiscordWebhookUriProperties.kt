package com.everyonewaiter.common.notification.discord

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordWebhookUriProperties(
    @Value("\${event.discord.webhook}") val eventWebhookUri: String,
    @Value("\${logging.discord.webhook}") val loggingWebhookUri: String,
)
