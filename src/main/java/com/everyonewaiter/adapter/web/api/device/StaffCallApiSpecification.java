package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.StaffCallDetailResponses;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponses;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.staffcall.StaffCallCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "직원 호출")
interface StaffCallApiSpecification {

  @Operation(summary = "[HALL] 직원 호출 목록 조회", description = "직원 호출 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "직원 호출 목록 조회 성공")
  @ApiErrorResponses(
      summary = "직원 호출 목록 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "인증 시그니처가 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 HALL이 아닌 경우"
          ),
      }
  )
  ResponseEntity<StaffCallDetailResponses> getStaffCalls(
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[TABLE] 직원 호출", description = "직원 호출 API")
  @ApiResponse(responseCode = "201", description = "직원 호출 성공")
  @ApiErrorResponses(
      summary = "직원 호출 실패",
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
              exampleName = "기기의 사용 용도가 TABLE이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STAFF_CALL_OPTION_NOT_FOUND,
              exampleName = "매장에 등록되어 있는 직원 호출 옵션명을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> create(
      @RequestBody StaffCallCreateRequest createRequest,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[HALL] 직원 호출 완료", description = "직원 호출 완료 API")
  @ApiResponse(responseCode = "204", description = "직원 호출 완료 성공")
  @ApiErrorResponses(
      summary = "직원 호출 완료 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.STORE_IS_CLOSED,
              exampleName = "매장이 영업중이지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_COMPLETED_STAFF_CALL,
              exampleName = "이미 완료된 직원 호출인 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "인증 시그니처가 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 HALL이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STAFF_CALL_NOT_FOUND,
              exampleName = "직원 호출을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> complete(
      Long staffCallId,
      @Parameter(hidden = true) Device device
  );

}
