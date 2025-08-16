package com.everyonewaiter.domain.store;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CountryOfOrigin")
public record CountryOfOrigin(
    @Schema(description = "품목", example = "돼지고기", requiredMode = REQUIRED)
    @NotBlank(message = "원산지 품목을 입력해 주세요.")
    @Size(min = 1, max = 10, message = "원산지 품목은 1자 이상 10자 이하로 입력해 주세요.")
    String item,

    @Schema(description = "원산지", example = "국내산", requiredMode = REQUIRED)
    @NotBlank(message = "원산지를 입력해 주세요.")
    @Size(min = 1, max = 10, message = "원산지는 1자 이상 10자 이하로 입력해 주세요.")
    String origin
) {

  private static final String COLON = ":";
  private static final String COMMA = ",";

  public CountryOfOrigin {
    validateInvalidCharacters(item);
    validateInvalidCharacters(origin);
  }

  private void validateInvalidCharacters(String value) {
    if (value.contains(COLON) || value.contains(COMMA)) {
      throw new IllegalArgumentException("원산지 정보에는 콜론(:)과 쉼표(,)를 포함할 수 없습니다.");
    }
  }

}
