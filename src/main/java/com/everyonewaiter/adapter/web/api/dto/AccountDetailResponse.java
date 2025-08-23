package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "AccountDetailResponse")
public record AccountDetailResponse(
    @Schema(description = "계정 ID", example = "\"694865267482835533\"")
    String accountId,

    @Schema(description = "이메일", example = "admin@everyonewaiter.com")
    String email,

    @Schema(description = "휴대폰 번호", example = "01044591812")
    String phoneNumber,

    @Schema(description = "상태", example = "ACTIVE")
    AccountState state,

    @Schema(description = "권한", example = "ADMIN")
    AccountPermission permission,

    @Schema(description = "마지막 로그인 시간", example = "2025-01-01 12:00:00")
    Instant lastSignIn,

    @Schema(description = "계정 생성일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "계정 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static AccountDetailResponse from(Account account) {
    return new AccountDetailResponse(
        String.valueOf(account.getId()),
        account.getEmail().address(),
        account.getPhoneNumber().value(),
        account.getState(),
        account.getPermission(),
        account.getLastSignIn(),
        account.getCreatedAt(),
        account.getUpdatedAt()
    );
  }

}
