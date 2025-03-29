package com.everyonewaiter.application.user.dto

import io.swagger.v3.oas.annotations.media.Schema

class AuthMail {
    @Schema(name = "AuthMail.SendRequest")
    data class SendRequest(
        @Schema(description = "이메일", example = "admin@everyonewaiter.com")
        val email: String,
    )
}
