package com.everyonewaiter.presentation.device;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE")
interface SseControllerSpecification {

  @Operation(summary = "[ALL] 매장 활동 알림 SSE 연결", description = "매장 활동 알림 SSE 연결 API")
  @ApiResponse(responseCode = "200", description = "매장 활동 알림 SSE 연결 성공")
  @ApiErrorResponses(
      summary = "매장 활동 알림 SSE 연결 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "인증 시그니처가 유효하지 않은 경우"
          ),
      }
  )
  ResponseEntity<SseEmitter> subscribe(
      String lastEventId,
      @Parameter(hidden = true) Device device,
      @Parameter(hidden = true) HttpServletResponse response
  );

}
