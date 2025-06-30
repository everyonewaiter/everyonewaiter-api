package com.everyonewaiter.presentation.device.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PosWriteRequest {

  @Schema(name = "PosWriteRequest.Discount")
  public record Discount(
      @Schema(description = "할인 금액", example = "10000", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "할인 금액이 누락되었습니다.")
      @Min(value = 0, message = "할인 금액은 0 이상으로 입력해 주세요.")
      long discountPrice
  ) {

  }

  @Schema(name = "PosWriteRequest.UpdateMemo")
  public record UpdateMemo(
      @Schema(description = "주문 메모", example = "13시 포장", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "주문 메모 정보가 누락되었습니다.")
      @Size(max = 30, message = "주문 메모는 30자 이하로 입력해 주세요.")
      String memo
  ) {

  }

}
