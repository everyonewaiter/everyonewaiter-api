package com.everyonewaiter.adapter.web.api.admin.request;

import com.everyonewaiter.application.account.request.AccountAdminWrite;
import com.everyonewaiter.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAdminWriteRequest {

  @Schema(name = "AccountAdminWriteRequest.Update")
  public record Update(
      @Schema(description = "상태", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "계정 상태가 누락되었거나 올바르지 않습니다.")
      Account.State state,

      @Schema(description = "권한", example = "ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "계정 권한이 누락되었거나 올바르지 않습니다.")
      Account.Permission permission
  ) {

    public AccountAdminWrite.Update toDomainDto() {
      return new AccountAdminWrite.Update(state, permission);
    }

  }

}
