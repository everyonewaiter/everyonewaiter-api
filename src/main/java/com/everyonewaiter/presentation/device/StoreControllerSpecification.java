package com.everyonewaiter.presentation.device;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "매장")
interface StoreControllerSpecification {

  @Operation(summary = "매장 오픈", description = "매장 오픈 API")
  @ApiResponse(responseCode = "204", description = "매장 오픈 성공")
  @ApiErrorResponses(
      summary = "매장 오픈 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> open(@Parameter(hidden = true) Device device);

  @Operation(summary = "매장 마감", description = "매장 마감 API")
  @ApiResponse(responseCode = "204", description = "매장 마감 성공")
  @ApiErrorResponses(
      summary = "매장 마감 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> close(@Parameter(hidden = true) Device device);

}
