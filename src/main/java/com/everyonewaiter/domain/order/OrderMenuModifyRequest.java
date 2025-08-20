package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "OrderMenuModifyRequest")
public record OrderMenuModifyRequest(
    @Schema(description = "메뉴 ID", example = "\"694865267482835533\"", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 ID가 누락되었습니다.")
    Long menuId,

    @Schema(description = "주문 수량", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "주문 수량이 누락되었습니다.")
    @Min(value = 1, message = "주문 수량은 1 이상으로 입력해 주세요.")
    Integer quantity,

    @Schema(description = "주문 메뉴 옵션 그룹", requiredMode = REQUIRED)
    @NotNull(message = "주문 메뉴 옵션 그룹 정보가 누락되었습니다.")
    @Size(max = 20, message = "주문 메뉴 옵션 그룹은 20개 이하로 선택해 주세요.")
    List<@Valid OrderOptionGroupModifyRequest> menuOptionGroups
) {

}
