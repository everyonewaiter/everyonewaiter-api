package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE")
interface SseApiSpecification {

  @Operation(summary = "[ALL] 매장 활동 알림 SSE 연결", description = "매장 활동 알림 SSE 연결 API")
  @ApiResponse(responseCode = "200", description = "매장 활동 알림 SSE 연결 성공")
  @ApiErrorResponse(
      summary = "매장 활동 알림 SSE 연결 실패",
      code = ErrorCode.UNAUTHORIZED,
      exampleName = "인증 시그니처가 유효하지 않은 경우"
  )
  ResponseEntity<SseEmitter> connect(
      String lastEventId,
      @Parameter(hidden = true) Device device,
      @Parameter(hidden = true) HttpServletResponse response
  );

}
