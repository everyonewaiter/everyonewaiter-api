package com.everyonewaiter.domain.menu;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CategoryCreateRequest")
public record CategoryCreateRequest(
    @Schema(description = "카테고리 이름", example = "스테이크", requiredMode = REQUIRED)
    @NotBlank(message = "카테고리 이름을 입력해 주세요.")
    @Size(min = 1, max = 20, message = "카테고리 이름은 1자 이상 20자 이하로 입력해 주세요.")
    String name
) {

}
