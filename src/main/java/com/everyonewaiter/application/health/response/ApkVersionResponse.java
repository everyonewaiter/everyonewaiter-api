package com.everyonewaiter.application.health.response;

import com.everyonewaiter.domain.health.entity.ApkVersion;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApkVersionResponse {

  @Schema(name = "ApkVersionResponse.Detail")
  public record Detail(
      @Schema(description = "메이저 버전", example = "1")
      int majorVersion,

      @Schema(description = "마이너 버전", example = "0")
      int minorVersion,

      @Schema(description = "패치 버전", example = "0")
      int patchVersion,

      @Schema(description = "APK 다운로드 링크", example = "https://download.everyonewaiter.com/app-release.apk")
      String downloadUrl,

      @Schema(description = "APK 버전 등록일", example = "2025-01-01 12:00:00")
      Instant createdAt
  ) {

    public static ApkVersionResponse.Detail from(ApkVersion apkVersion) {
      return new ApkVersionResponse.Detail(
          apkVersion.getMajorVersion(),
          apkVersion.getMinorVersion(),
          apkVersion.getPatchVersion(),
          apkVersion.getDownloadUrl(),
          apkVersion.getCreatedAt()
      );
    }

  }

}
