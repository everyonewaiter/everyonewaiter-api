package com.everyonewaiter.application.account.dto;

import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "AccountAdminReadResponse")
public record AccountAdminReadResponse(
    @Schema(description = "계정 ID", example = "\"694865267482835533\"")
    String accountId,

    @Schema(description = "이메일", example = "admin@everyonewaiter.com")
    String email,

    @Schema(description = "상태", example = "ACTIVE")
    AccountState state,

    @Schema(description = "권한", example = "ADMIN")
    AccountPermission permission,

    @Schema(description = "매장 소유 여부", examples = {"Y", "N"})
    char hasStore,

    @Schema(description = "계정 생성일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "계정 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static AccountAdminReadResponse from(AccountAdminPageView adminPageView) {
    return new AccountAdminReadResponse(
        adminPageView.id().toString(),
        adminPageView.email(),
        adminPageView.state(),
        adminPageView.permission(),
        adminPageView.hasStore() ? 'Y' : 'N',
        adminPageView.createdAt(),
        adminPageView.updatedAt()
    );
  }

}
