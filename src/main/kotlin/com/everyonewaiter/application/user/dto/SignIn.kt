package com.everyonewaiter.application.user.dto

import io.swagger.v3.oas.annotations.media.Schema

class SignIn {
    @Schema(name = "SignIn.Request")
    data class Request(
        @Schema(description = "이메일", example = "admin@everyonewaiter.com")
        val email: String,
        @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1")
        val password: String,
    )

    @Schema(name = "SignIn.Response")
    data class Response(
        @Schema(description = "액세스 토큰", example = "abcdefghijklmnopqrstuvwxyz")
        val accessToken: String,
    )
}
