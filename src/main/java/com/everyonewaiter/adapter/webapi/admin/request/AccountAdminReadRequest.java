package com.everyonewaiter.adapter.webapi.admin.request;

import com.everyonewaiter.application.account.request.AccountAdminRead;
import com.everyonewaiter.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAdminReadRequest {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PageView {

    @Schema(description = "조회할 이메일")
    private String email;

    @Schema(description = "조회할 상태")
    private Account.State state;

    @Schema(description = "조회할 권한")
    private Account.Permission permission;

    @Schema(description = "조회할 매장 소유 여부")
    private Boolean hasStore;

    @Schema(description = "조회 페이지 번호", defaultValue = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    @Max(value = 1_000, message = "페이지 번호는 1,000 이하이어야 합니다.")
    private long page = 1;

    @Schema(description = "페이지 조회 데이터 수", defaultValue = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "페이지 조회 크기는 1 이상이어야 합니다.")
    @Max(value = 100, message = "페이지 조회 크기는 100 이하이어야 합니다.")
    private long size = 20;

    public AccountAdminRead.PageView toDomainDto() {
      return new AccountAdminRead.PageView(email, state, permission, hasStore, page, size);
    }

  }

}
