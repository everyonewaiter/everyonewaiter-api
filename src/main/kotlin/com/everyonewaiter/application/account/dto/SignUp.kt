package com.everyonewaiter.application.account.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

class SignUp {
    @Schema(name = "SignUp.Request")
    data class Request(
        @Schema(description = "이메일", example = "admin@everyonewaiter.com")
        @field:Pattern(regexp = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+\$", message = "잘못된 형식의 이메일을 입력하셨어요. 이메일을 다시 입력해 주세요.")
        val email: String,
        @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1")
        @field:Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 조합하여 8자리 이상으로 입력해 주세요.",
        )
        val password: String,
        @Schema(description = "휴대폰 번호", example = "01044591812")
        @field:Pattern(regexp = "^01[016789]\\d{7,8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
        val phoneNumber: String,
    )
}
