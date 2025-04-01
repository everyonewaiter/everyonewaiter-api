package com.everyonewaiter.application.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

class SendAuthMail {
    @Schema(name = "SendAuthMail.Request")
    data class Request(
        @Schema(description = "이메일", example = "admin@everyonewaiter.com")
        @field:Pattern(regexp = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+\$", message = "잘못된 형식의 이메일을 입력하셨어요. 이메일을 다시 입력해 주세요.")
        val email: String,
    )
}
