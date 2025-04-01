package com.everyonewaiter.application.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern

class VerifyAuthCode {
    @Schema(name = "VerifyAuthCode.Request")
    data class Request(
        @Schema(description = "휴대폰 번호", example = "01044591812")
        @field:Pattern(regexp = "^01[016789]\\d{7,8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
        val phoneNumber: String,
        @Schema(description = "인증 번호", example = "123456")
        @field:Min(value = 100000, message = "인증 번호는 6자리 숫자로 입력해 주세요.")
        @field:Max(value = 999999, message = "인증 번호는 6자리 숫자로 입력해 주세요.")
        val code: Int,
    )
}
