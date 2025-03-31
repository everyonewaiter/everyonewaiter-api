package com.everyonewaiter.application.auth.service

import com.everyonewaiter.common.notification.message.AlimTalkMessage
import com.everyonewaiter.common.notification.message.MessageClient
import com.everyonewaiter.domain.auth.event.AuthCodeCreateEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class AuthCodeSender(
    private val messageClient: MessageClient,
) {
    @Async
    @EventListener
    fun sendCode(event: AuthCodeCreateEvent) {
        val message = AlimTalkMessage(
            content =
                """
                [모두의 웨이터]
                
                인증번호는 [${event.code}]입니다.
                """.trimIndent(),
            to = event.phoneNumber,
        )
        messageClient.sendAlimTalk("authenticationCode", message)
    }
}
