package com.everyonewaiter.adapter.webapi.owner;

import com.everyonewaiter.application.health.dto.ApkVersionDetailResponse;
import com.everyonewaiter.application.health.dto.ServerVersionDetailResponse;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Health Check")
interface HealthCheckApiSpecification {

  @SecurityRequirements
  @Operation(summary = "서버 버전 조회", description = "서버 버전 조회 API")
  @ApiResponse(responseCode = "200", description = "서버 버전 조회 성공")
  ResponseEntity<ServerVersionDetailResponse> getServerVersion();

  @SecurityRequirements
  @Operation(summary = "APK 버전 조회", description = "APK 버전 조회 API")
  @ApiResponse(responseCode = "200", description = "APK 버전 조회 성공")
  @ApiErrorResponse(
      summary = "APK 버전 조회 실패",
      code = ErrorCode.APK_VERSION_NOT_FOUND,
      exampleName = "APK 버전 정보를 찾을 수 없는 경우"
  )
  ResponseEntity<ApkVersionDetailResponse> getLatestApkVersion();

}
