package com.everyonewaiter.common.notification.mail

interface MailClient {
    fun sendTo(
        to: String,
        subject: String,
        content: String,
    ): Result<Unit>

    fun sendTo(
        to: List<String>,
        subject: String,
        content: String,
    ): Result<Unit>
}
