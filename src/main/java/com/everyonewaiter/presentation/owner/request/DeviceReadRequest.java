package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.device.request.DeviceRead;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceReadRequest {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PageView {

    @Schema(description = "조회 페이지 번호", defaultValue = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    @Max(value = 1_000, message = "페이지 번호는 1,000 이하이어야 합니다.")
    private long page = 1;

    @Schema(description = "페이지 조회 데이터 수", defaultValue = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "페이지 조회 크기는 1 이상이어야 합니다.")
    @Max(value = 100, message = "페이지 조회 크기는 100 이하이어야 합니다.")
    private long size = 20;

    public DeviceRead.PageView toDomainDto() {
      return new DeviceRead.PageView(page, size);
    }

  }

}
