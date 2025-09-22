package com.everyonewaiter.domain.store;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(name = "StoreUpdateRequest")
public record StoreUpdateRequest(
    @Schema(description = "매장 전화번호", example = "02-123-4567", requiredMode = REQUIRED)
    @NotBlank(message = "매장 전화번호를 입력해 주세요.")
    @Pattern(
        regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
        message = "잘못된 형식의 매장 전화번호를 입력하셨어요. 매장 전화번호를 다시 입력해 주세요."
    )
    String landline,

    @Valid
    @NotNull(message = "매장 설정 정보가 누락되었습니다.")
    StoreSettingUpdateRequest setting
) {

}
