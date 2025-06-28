package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.store.response.StoreResponse;
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

  @Operation(summary = "[ALL] 매장 영업 상태 조회", description = "매장 영업 상태 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 영업 상태 조회 성공")
  @ApiErrorResponses(
      summary = "매장 영업 상태 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "기기에 등록되어 있는 매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<StoreResponse.SimpleWithStatus> getStore(@Parameter(hidden = true) Device device);

  @Operation(summary = "[POS] 매장 오픈", description = "매장 오픈 API")
  @ApiResponse(responseCode = "204", description = "매장 오픈 성공")
  @ApiErrorResponses(
      summary = "매장 오픈 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_STORE_OPENED,
              exampleName = "이미 매장이 영업중인 경우"
          ),
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
              exampleName = "기기에 등록되어 있는 매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> open(@Parameter(hidden = true) Device device);

  @Operation(summary = "[POS] 매장 마감", description = "매장 마감 API")
  @ApiResponse(responseCode = "204", description = "매장 마감 성공")
  @ApiErrorResponses(
      summary = "매장 마감 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_STORE_OPENED,
              exampleName = "이미 매장이 마감되어 있는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.INCOMPLETE_POS_TABLE_ACTIVITY,
              exampleName = "완료되지 않은 주문이 있는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.INCOMPLETE_WAITING,
              exampleName = "완료되지 않은 웨이팅이 있는 경우"
          ),
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
              exampleName = "기기에 등록되어 있는 매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> close(@Parameter(hidden = true) Device device);

}
