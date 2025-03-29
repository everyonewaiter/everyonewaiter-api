package com.everyonewaiter.application.user.dto

import io.swagger.v3.oas.annotations.media.Schema

class AuthCode {
    @Schema(name = "AuthCode.SendRequest")
    data class SendRequest(
        @Schema(description = "휴대폰 번호", example = "01044591812")
        val phoneNumber: String,
    )

    @Schema(name = "AuthCode.VerifyRequest")
    data class VerifyRequest(
        @Schema(description = "휴대폰 번호", example = "01044591812")
        val phoneNumber: String,
        @Schema(description = "인증 번호", example = "123456")
        val code: Int,
    )
}
