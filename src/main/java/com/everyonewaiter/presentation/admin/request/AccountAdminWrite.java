package com.everyonewaiter.presentation.admin.request;

import com.everyonewaiter.application.account.request.AccountAdminUpdate;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.Enum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAdminWrite {

  @Schema(name = "AccountAdminWrite.UpdateRequest")
  public record UpdateRequest(
      @Schema(description = "상태", example = "ACTIVE")
      @NotNull(message = "계정 상태가 누락되었거나 옳바르지 않습니다.")
      @Enum(clazz = Account.State.class, message = "계정 상태가 누락되었거나 옳바르지 않습니다.")
      Account.State state,

      @Schema(description = "권한", example = "ADMIN")
      @NotNull(message = "계정 권한이 누락되었거나 옳바르지 않습니다.")
      @Enum(clazz = Account.Permission.class, message = "계정 권한이 누락되었거나 옳바르지 않습니다.")
      Account.Permission permission
  ) {

    public AccountAdminUpdate toDomainDto() {
      return new AccountAdminUpdate(state, permission);
    }

  }

}
