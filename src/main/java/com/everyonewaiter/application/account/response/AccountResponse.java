package com.everyonewaiter.application.account.response;

import com.everyonewaiter.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountResponse {

  @Schema(name = "AccountResponse.Profile")
  public record Profile(
      @Schema(description = "계정 ID", example = "\"694865267482835533\"")
      String accountId,

      @Schema(description = "이메일", example = "admin@everyonewaiter.com")
      String email,

      @Schema(description = "권한", example = "ADMIN")
      Account.Permission permission
  ) {

    public static Profile from(Account account) {
      return new Profile(
          Objects.requireNonNull(account.getId()).toString(),
          account.getEmail(),
          account.getPermission()
      );
    }

  }

}
