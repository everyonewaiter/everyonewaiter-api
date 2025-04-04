package com.everyonewaiter.common.notification.discord

data class DiscordWebhookPayload(
    val embeds: List<DiscordEmbed>,
)

data class DiscordEmbed(
    val title: String,
    val description: String,
    val color: Int = 0,
    val fields: List<DiscordField> = listOf(),
)

data class DiscordField(
    val name: String,
    val value: String,
)
