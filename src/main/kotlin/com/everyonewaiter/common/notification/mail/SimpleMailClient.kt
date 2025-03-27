package com.everyonewaiter.common.notification.mail

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

private const val NO_REPLY = "모두의 웨이터 <noreply@everyonewaiter.com>"

@Component
internal class SimpleMailClient(
    private val javaMailSender: JavaMailSender,
) : MailClient {
    override fun sendTo(
        to: String,
        subject: String,
        content: String,
    ) = runCatching {
        val message = javaMailSender.createMimeMessage()
        MimeMessageHelper(message, true, StandardCharsets.UTF_8.name()).apply {
            setFrom(NO_REPLY)
            setTo(to)
            setSubject(subject)
            setText(content, true)
        }
        javaMailSender.send(message)
    }

    override fun sendTo(
        to: List<String>,
        subject: String,
        content: String,
    ) = runCatching {
        val message = javaMailSender.createMimeMessage()
        MimeMessageHelper(message, true, StandardCharsets.UTF_8.name()).apply {
            setFrom(NO_REPLY)
            setBcc(to.toTypedArray())
            setSubject(subject)
            setText(content, true)
        }
        javaMailSender.send(message)
    }
}
