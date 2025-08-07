package com.everyonewaiter.adapter.webapi.device;

import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "기기")
interface DeviceControllerSpecification {

  @Operation(summary = "[ALL] 기기 상세 조회", description = "기기 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "기기 상세 조회 성공")
  @ApiErrorResponse(
      summary = "기기 상세 조회 실패",
      code = ErrorCode.UNAUTHORIZED,
      exampleName = "인증 시그니처가 유효하지 않은 경우"
  )
  ResponseEntity<DeviceResponse.Detail> getDevice(@Parameter(hidden = true) Device device);

}
