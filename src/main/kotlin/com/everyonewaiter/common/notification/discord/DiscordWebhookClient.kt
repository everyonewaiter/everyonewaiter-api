package com.everyonewaiter.common.notification.discord

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "discord-webhook-client", url = "https://discord.com/api/webhooks")
fun interface DiscordWebhookClient {
    @PostMapping(value = ["/{discordWebhookUri}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun send(
        @PathVariable("discordWebhookUri") uri: String,
        @RequestBody payload: DiscordWebhookPayload,
    )
}
