package com.everyonewaiter.application.auth.service

import com.everyonewaiter.common.jwt.JwtPayload
import com.everyonewaiter.common.jwt.JwtProvider
import com.everyonewaiter.common.notification.mail.MailClient
import com.everyonewaiter.common.notification.mail.MailTemplateReader
import com.everyonewaiter.domain.account.event.AccountCreateEvent
import com.everyonewaiter.global.config.AllowClientConfiguration
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

private const val ONE_DAY_MILLISECONDS = 1000L * 60L * 60L * 24L

private val logger = KotlinLogging.logger {}

@Service
class AuthMailSender(
    private val allowClientConfiguration: AllowClientConfiguration,
    private val mailClient: MailClient,
    private val mailTemplateReader: MailTemplateReader,
    private val jwtProvider: JwtProvider,
) {
    @Async
    @TransactionalEventListener
    fun sendMail(event: AccountCreateEvent) {
        val baseUrl = allowClientConfiguration.baseUrl
        val accessToken = jwtProvider.generate(JwtPayload(event.email, "auth-mail"), ONE_DAY_MILLISECONDS)
        val authUrl = "$baseUrl/auth/mail?email=${event.email}&accessToken=$accessToken"
        val content = mailTemplateReader.read("mail/email-authentication", mapOf("authenticationUrl" to authUrl))
        mailClient
            .sendTo(
                to = event.email,
                subject = "[모두의 웨이터] 이메일 인증 안내드립니다.",
                content = content,
            ).onSuccess { logger.info { "이메일 인증 메일을 발송했습니다. [email:${event.email}]" } }
            .onFailure { logger.warn { "이메일 인증 메일 발송에 실패했습니다. [email:${event.email}] [${it.message}]" } }
    }
}
