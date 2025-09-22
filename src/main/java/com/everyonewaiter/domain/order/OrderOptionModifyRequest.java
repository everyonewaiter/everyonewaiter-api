package com.everyonewaiter.domain.order;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "OrderOptionModifyRequest")
public record OrderOptionModifyRequest(
    @Schema(description = "메뉴 옵션명", example = "밑반찬 주세요 O", requiredMode = REQUIRED)
    @NotBlank(message = "메뉴 옵션명을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "메뉴 옵션명은 1자 이상 30자 이하로 입력해 주세요.")
    String name,

    @Schema(description = "메뉴 옵션 가격", example = "1000", requiredMode = REQUIRED)
    @NotNull(message = "메뉴 옵션 가격 정보가 누락되었습니다.")
    @Min(value = -10_000_000, message = "메뉴 옵션 가격은 -10,000,000 이상으로 입력해 주세요.")
    @Max(value = 10_000_000, message = "메뉴 옵션 가격은 10,000,000 이하로 입력해 주세요.")
    long price
) {

}
