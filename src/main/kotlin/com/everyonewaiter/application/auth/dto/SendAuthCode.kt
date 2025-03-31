package com.everyonewaiter.application.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

class SendAuthCode {
    @Schema(name = "SendAuthCode.Request")
    data class Request(
        @Schema(description = "휴대폰 번호", example = "01044591812")
        @field:Pattern(regexp = "^01[016789]\\d{7,8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
        val phoneNumber: String,
    )
}
