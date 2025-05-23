package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "POS")
interface PosControllerSpecification {

  @Operation(summary = "[POS] 테이블 목록 조회", description = "테이블 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "테이블 목록 조회 성공")
  @ApiErrorResponses(
      summary = "테이블 목록 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.STORE_IS_CLOSED,
              exampleName = "매장이 영업중이지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "인증 시그니처가 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
      }
  )
  ResponseEntity<PosResponse.Tables> getTables(@Parameter(hidden = true) Device device);

}
