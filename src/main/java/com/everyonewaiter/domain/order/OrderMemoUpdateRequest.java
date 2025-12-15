package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "PosTableUpdateOrderMemoRequest")
public record OrderMemoUpdateRequest(
    @Schema(description = "주문 메모", example = "13시 포장", requiredMode = REQUIRED)
    @NotNull(message = "주문 메모 정보가 누락되었습니다.")
    @Size(max = 10, message = "주문 메모는 10자 이하로 입력해 주세요.")
    String memo
) {

}
