package com.everyonewaiter.presentation.admin.request;

import com.everyonewaiter.application.store.request.RegistrationAdminWrite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationAdminWriteRequest {

  @Schema(name = "RegistrationAdminWriteRequest.Reject")
  public record Reject(
      @Schema(description = "매장 등록 거부 사유", example = "사업자 정보를 조회할 수 없습니다.")
      @NotBlank(message = "매장 등록 거부 사유를 입력해 주세요.")
      @Size(min = 1, max = 30, message = "매장 등록 거부 사유는 1자 이상 30자 이하로 입력해 주세요.")
      String reason
  ) {

    public RegistrationAdminWrite.Reject toDomainDto() {
      return new RegistrationAdminWrite.Reject(reason);
    }

  }

}
