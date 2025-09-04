package com.everyonewaiter.domain.staffcall;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "StaffCallRequest")
public record StaffCallRequest(
    @Schema(description = "직원 호출 옵션명", example = "직원 호출", requiredMode = REQUIRED)
    @NotBlank(message = "직원 호출 옵션명을 입력해 주세요.")
    @Size(min = 1, max = 10, message = "직원 호출 옵션명은 1자 이상 10자 이하로 입력해 주세요.")
    String optionName
) {

}
