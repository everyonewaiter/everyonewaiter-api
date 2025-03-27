package com.everyonewaiter.common.notification.message

import org.springframework.stereotype.Component

@Component
class MessageClient(
    private val client: NaverSensClient,
    private val properties: NaverSensProperties,
) {
    fun sendAlimTalk(
        templateCode: String,
        messages: AlimTalkMessage,
    ) {
        sendAlimTalk(templateCode, listOf(messages))
    }

    fun sendAlimTalk(
        templateCode: String,
        messages: List<AlimTalkMessage>,
    ) {
        val payload = AlimTalkSendPayload(templateCode, properties.channelId, messages)
        client.sendAlimTalk(properties.serviceId, payload)
    }
}
