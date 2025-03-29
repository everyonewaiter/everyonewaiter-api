package com.everyonewaiter.application.user.dto

import io.swagger.v3.oas.annotations.media.Schema

class SignUp {
    @Schema(name = "SignUp.Request")
    data class Request(
        @Schema(description = "이메일", example = "admin@everyonewaiter.com")
        val email: String,
        @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1")
        val password: String,
        @Schema(description = "휴대폰 번호", example = "01044591812")
        val phoneNumber: String,
    )
}
