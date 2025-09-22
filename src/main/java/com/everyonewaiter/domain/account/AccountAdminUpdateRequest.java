package com.everyonewaiter.domain.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AccountAdminUpdateRequest")
public record AccountAdminUpdateRequest(
    @Schema(description = "상태", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "계정 상태가 누락되었거나 올바르지 않습니다.")
    AccountState state,

    @Schema(description = "권한", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "계정 권한이 누락되었거나 올바르지 않습니다.")
    AccountPermission permission
) {

}
