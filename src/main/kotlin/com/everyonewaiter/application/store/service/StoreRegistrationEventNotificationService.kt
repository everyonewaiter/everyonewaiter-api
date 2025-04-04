package com.everyonewaiter.application.store.service

import com.everyonewaiter.common.notification.discord.DiscordEmbed
import com.everyonewaiter.common.notification.discord.DiscordWebhookClient
import com.everyonewaiter.common.notification.discord.DiscordWebhookPayload
import com.everyonewaiter.common.notification.discord.DiscordWebhookUriProperties
import com.everyonewaiter.domain.store.event.StoreRegistrationApplyEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class StoreRegistrationEventNotificationService(
    private val discordWebhookClient: DiscordWebhookClient,
    private val discordWebhookUriProperties: DiscordWebhookUriProperties,
) {
    @Async
    @TransactionalEventListener
    fun consumeStoreRegistrationApplyEvent(event: StoreRegistrationApplyEvent) {
        discordWebhookClient.send(
            uri = discordWebhookUriProperties.eventWebhookUri,
            payload = DiscordWebhookPayload(
                listOf(
                    DiscordEmbed(
                        title = "매장 등록 신청 이벤트",
                        description = "${event.name} 사장님께서 매장 등록을 신청하셨습니다!",
                        color = 3447003,
                    ),
                ),
            ),
        )
    }
}
