package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "OrderOptionGroupModifyRequest")
public record OrderOptionGroupModifyRequest(
    @Schema(description = "메뉴 옵션 그룹 ID", example = "\"694865267482835533\"", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 옵션 그룹 ID가 누락되었습니다.")
    Long menuOptionGroupId,

    @Schema(description = "주문 메뉴 옵션", requiredMode = REQUIRED)
    @NotNull(message = "주문 메뉴 옵션 정보가 누락되었습니다.")
    @Size(max = 20, message = "주문 메뉴 옵션은 20개 이하로 선택해 주세요.")
    List<@Valid OrderOptionModifyRequest> orderOptions
) {

}
