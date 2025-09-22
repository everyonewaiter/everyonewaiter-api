package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "OrderUpdateRequest")
public record OrderUpdateRequest(
    @Schema(description = "주문 ID", example = "694865267482835533", requiredMode = REQUIRED)
    @NotNull(message = "주문 ID가 누락되었습니다.")
    Long orderId,

    @Schema(description = "주문 메뉴 목록", requiredMode = REQUIRED)
    @NotNull(message = "주문 메뉴 목록이 누락되었습니다.")
    @Size(min = 1, message = "주문 메뉴 목록을 1개 이상 입력해 주세요.")
    List<@Valid OrderMenuQuantityUpdateRequest> orderMenus
) {

}
