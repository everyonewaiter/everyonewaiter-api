package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.DeviceDetailResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "기기")
interface DeviceApiSpecification {

  @Operation(summary = "[ALL] 기기 상세 조회", description = "기기 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "기기 상세 조회 성공")
  @ApiErrorResponse(
      summary = "기기 상세 조회 실패",
      code = ErrorCode.UNAUTHORIZED,
      exampleName = "인증 시그니처가 유효하지 않은 경우"
  )
  ResponseEntity<DeviceDetailResponse> getDevice(@Parameter(hidden = true) Device device);

}
