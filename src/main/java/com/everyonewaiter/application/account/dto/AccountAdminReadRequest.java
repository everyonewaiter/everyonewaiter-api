package com.everyonewaiter.application.account.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountAdminReadRequest {

  @Nullable
  @Schema(description = "조회할 이메일")
  private String email;

  @Nullable
  @Schema(description = "조회할 상태")
  private AccountState state;

  @Nullable
  @Schema(description = "조회할 권한")
  private AccountPermission permission;

  @Nullable
  @Schema(description = "조회할 매장 소유 여부")
  private Boolean hasStore;

  @Schema(description = "조회 페이지 번호", defaultValue = "1", requiredMode = REQUIRED)
  @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
  @Max(value = 1_000, message = "페이지 번호는 1,000 이하이어야 합니다.")
  private long page = 1;

  @Schema(description = "페이지 조회 데이터 수", defaultValue = "20", requiredMode = REQUIRED)
  @Min(value = 1, message = "페이지 조회 크기는 1 이상이어야 합니다.")
  @Max(value = 100, message = "페이지 조회 크기는 100 이하이어야 합니다.")
  private long size = 20;

}
