package com.everyonewaiter.adapter.web.api.admin;

import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponses;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "헬스 체크")
interface HealthCheckAdminApiSpecification {

  @Operation(summary = "APK 버전 생성", description = "APK 버전 생성 API")
  @ApiResponse(responseCode = "201", description = "APK 버전 생성 성공")
  @ApiErrorResponses(
      summary = "APK 버전 생성 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "관리자 권한이 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> createApkVersion(
      @RequestBody ApkVersionCreateRequest request,
      @Parameter(hidden = true) Account account
  );

}
