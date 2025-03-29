package com.everyonewaiter.common.notification.discord

import com.everyonewaiter.support.IntegrationTest
import io.kotest.core.spec.style.FunSpec

@IntegrationTest
class DiscordWebhookClientTest(
    private val discordWebhookClient: DiscordWebhookClient,
) : FunSpec({
        xcontext("send") {
            test("디스코드 메시지를 전송한다.") {
                val payload = DiscordWebhookPayload(
                    listOf(
                        DiscordEmbed(
                            title = "제목",
                            description = "설명",
                            color = 16711680,
                            fields = listOf(
                                DiscordField("모두의 웨이터", "Hello World"),
                            ),
                        ),
                    ),
                )
                discordWebhookClient.send("<DISCORD_WEBHOOK_URI>", payload)
            }
        }
    })
