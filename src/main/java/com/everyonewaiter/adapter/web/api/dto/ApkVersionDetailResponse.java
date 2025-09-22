package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.health.ApkVersion;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "ApkVersionDetailResponse")
public record ApkVersionDetailResponse(
    @Schema(description = "메이저 버전", example = "1")
    int majorVersion,

    @Schema(description = "마이너 버전", example = "0")
    int minorVersion,

    @Schema(description = "패치 버전", example = "0")
    int patchVersion,

    @Schema(description = "APK 다운로드 링크", example = "https://cdn.everyonewaiter.com/app-release.apk")
    String downloadUrl,

    @Schema(description = "APK 버전 등록일", example = "2025-01-01 12:00:00")
    Instant createdAt
) {

  public static ApkVersionDetailResponse from(ApkVersion apkVersion) {
    return new ApkVersionDetailResponse(
        apkVersion.getMajorVersion(),
        apkVersion.getMinorVersion(),
        apkVersion.getPatchVersion(),
        apkVersion.getDownloadUri(),
        apkVersion.getCreatedAt()
    );
  }

}
