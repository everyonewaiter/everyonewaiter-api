package com.everyonewaiter.application.account.response;

import com.everyonewaiter.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(name = "Account.ProfileResponse")
public record ProfileResponse(
    @Schema(description = "계정 ID", example = "\"694865267482835533\"")
    String accountId,

    @Schema(description = "이메일", example = "admin@everyonewaiter.com")
    String email,

    @Schema(description = "권한", example = "ADMIN")
    Account.Permission permission
) {

  public static ProfileResponse from(Account account) {
    return new ProfileResponse(
        Objects.requireNonNull(account.getId()).toString(),
        account.getEmail(),
        account.getPermission()
    );
  }

}
