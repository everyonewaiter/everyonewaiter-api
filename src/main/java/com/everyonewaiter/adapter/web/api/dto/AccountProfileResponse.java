package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AccountProfileResponse")
public record AccountProfileResponse(
    @Schema(description = "계정 ID", example = "\"694865267482835533\"")
    String accountId,

    @Schema(description = "이메일", example = "admin@everyonewaiter.com")
    String email,

    @Schema(description = "권한", example = "ADMIN")
    AccountPermission permission
) {

  public static AccountProfileResponse from(Account account) {
    return new AccountProfileResponse(
        account.getNonNullId().toString(),
        account.getEmail().address(),
        account.getPermission()
    );
  }

}
