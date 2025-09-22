package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "OrderMenuQuantityUpdateRequest")
public record OrderMenuQuantityUpdateRequest(
    @Schema(description = "주문 메뉴 ID", example = "694865267482835533", requiredMode = REQUIRED)
    @NotNull(message = "주문 메뉴 ID가 누락되었습니다.")
    Long orderMenuId,

    @Schema(description = "주문 메뉴 수량", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "주문 메뉴 수량이 누락되었습니다.")
    @Min(value = 0, message = "주문 메뉴 수량은 0 이상으로 입력해 주세요.")
    int quantity
) {

}
