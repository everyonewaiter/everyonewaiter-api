package com.everyonewaiter.application.contact.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.everyonewaiter.domain.contact.ContactState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactAdminReadRequest {

  @Schema(description = "조회할 상호명")
  private String storeName;

  @Schema(description = "조회할 전화번호")
  private String phoneNumber;

  @Schema(description = "조회할 사업자번호")
  private String license;

  @Schema(description = "조회할 상태")
  private ContactState state;

  @Schema(description = "조회 페이지 번호", defaultValue = "1", requiredMode = REQUIRED)
  @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
  @Max(value = 1_000, message = "페이지 번호는 1,000 이하이어야 합니다.")
  private long page = 1;

  @Schema(description = "페이지 조회 데이터 수", defaultValue = "20", requiredMode = REQUIRED)
  @Min(value = 1, message = "페이지 조회 크기는 1 이상이어야 합니다.")
  @Max(value = 100, message = "페이지 조회 크기는 100 이하이어야 합니다.")
  private long size = 20;

}
