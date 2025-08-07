package com.everyonewaiter.domain.health;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ApkVersionCreateRequest")
public record ApkVersionCreateRequest(
    @Schema(description = "메이저 버전", example = "1", requiredMode = REQUIRED)
    @Min(value = 1, message = "메이저 버전은 1 이상이어야 합니다.")
    @Max(value = 100, message = "메이저 버전은 100 이하이어야 합니다.")
    int majorVersion,

    @Schema(description = "마이너 버전", example = "0", requiredMode = REQUIRED)
    @Min(value = 0, message = "마이너 버전은 0 이상이어야 합니다.")
    @Max(value = 100, message = "마이너 버전은 100 이하이어야 합니다.")
    int minorVersion,

    @Schema(description = "패치 버전", example = "0", requiredMode = REQUIRED)
    @Min(value = 0, message = "패치 버전은 0 이상이어야 합니다.")
    @Max(value = 100, message = "패치 버전은 100 이하이어야 합니다.")
    int patchVersion,

    @Schema(description = "APK 다운로드 링크", example = "https://cdn.everyonewaiter.com", requiredMode = REQUIRED)
    @NotNull(message = "APK 다운로드 링크가 누락되었거나 올바르지 않습니다.")
    String downloadUrl
) {

}
