package com.everyonewaiter.adapter.web.api.common;

import com.everyonewaiter.adapter.web.api.owner.dto.ApkVersionDetailResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.domain.health.ServerInfo;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "헬스 체크")
interface HealthCheckApiSpecification {

  @SecurityRequirements
  @Operation(summary = "서버 버전 조회", description = "서버 버전 조회 API")
  @ApiResponse(responseCode = "200", description = "서버 버전 조회 성공")
  ResponseEntity<ServerInfo> getServerVersion();

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
