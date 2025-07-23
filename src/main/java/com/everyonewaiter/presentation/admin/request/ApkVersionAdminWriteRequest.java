package com.everyonewaiter.presentation.admin.request;

import com.everyonewaiter.application.health.request.ApkVersionWrite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApkVersionAdminWriteRequest {

  @Schema(name = "ApkVersionAdminWriteRequest.Create")
  public record Create(
      @Schema(description = "메이저 버전", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
      @Min(value = 1, message = "메이저 버전은 1 이상이어야 합니다.")
      @Max(value = 100, message = "메이저 버전은 100 이하이어야 합니다.")
      int majorVersion,

      @Schema(description = "마이너 버전", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
      @Min(value = 0, message = "마이너 버전은 0 이상이어야 합니다.")
      @Max(value = 100, message = "마이너 버전은 100 이하이어야 합니다.")
      int minorVersion,

      @Schema(description = "패치 버전", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
      @Min(value = 0, message = "패치 버전은 0 이상이어야 합니다.")
      @Max(value = 100, message = "패치 버전은 100 이하이어야 합니다.")
      int patchVersion,

      @Schema(description = "APK 다운로드 링크", example = "https://everyonewaiter.com", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotNull(message = "APK 다운로드 링크가 누락되었거나 올바르지 않습니다.")
      String downloadUrl
  ) {

    public ApkVersionWrite.Create toDomainDto() {
      return new ApkVersionWrite.Create(majorVersion, minorVersion, patchVersion, downloadUrl);
    }

  }

}
