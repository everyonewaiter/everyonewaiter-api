package com.everyonewaiter.application.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

class SignIn {
    @Schema(name = "SignIn.Request")
    data class Request(
        @Schema(description = "이메일", example = "admin@everyonewaiter.com")
        @field:Pattern(regexp = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+\$", message = "이메일 및 비밀번호를 확인해 주세요.")
        val email: String,
        @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1")
        @field:Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
            message = "이메일 및 비밀번호를 확인해 주세요.",
        )
        val password: String,
    )

    @Schema(name = "SignIn.Response")
    data class Response(
        @Schema(description = "액세스 토큰", example = "abcdefghijklmnopqrstuvwxyz")
        val accessToken: String,
    )
}
