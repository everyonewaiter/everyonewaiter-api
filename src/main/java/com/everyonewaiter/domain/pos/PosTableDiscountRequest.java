package com.everyonewaiter.domain.pos;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "PosTableDiscountRequest")
public record PosTableDiscountRequest(
    @Schema(description = "할인 금액", example = "10000", requiredMode = REQUIRED)
    @NotNull(message = "할인 금액이 누락되었습니다.")
    @Min(value = 0, message = "할인 금액은 0 이상으로 입력해 주세요.")
    long discountPrice
) {

}
