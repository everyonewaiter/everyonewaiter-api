package com.everyonewaiter.application.account.dto

import com.everyonewaiter.domain.account.entity.AccountPermission
import io.swagger.v3.oas.annotations.media.Schema

class Profile {
    @Schema(name = "Profile.Response")
    data class Response(
        @Schema(description = "계정 ID", example = "\"694865267482835533\"")
        val accountId: String,
        @Schema(description = "이메일", example = "admin@everyonewaiter.com")
        val email: String,
        @Schema(description = "권한", example = "ADMIN")
        val permission: AccountPermission,
    )
}
