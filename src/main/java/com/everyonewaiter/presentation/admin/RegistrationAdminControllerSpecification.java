package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.store.response.RegistrationAdmin;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.support.Paging;
import com.everyonewaiter.presentation.admin.request.RegistrationAdminReadRequest;
import com.everyonewaiter.presentation.admin.request.RegistrationAdminWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "매장 등록 관리")
interface RegistrationAdminControllerSpecification {

  @Operation(summary = "등록 신청 목록 조회", description = "매장 등록 신청 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 등록 신청 목록 조회 성공")
  @ApiErrorResponses(
      summary = "매장 등록 신청 목록 조회 실패",
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
  ResponseEntity<Paging<RegistrationAdmin.PageViewResponse>> getRegistrations(
      @ParameterObject RegistrationAdminReadRequest.PageView request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "등록 신청 상세 조회", description = "매장 등록 신청 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 등록 신청 상세 조회 성공")
  @ApiErrorResponses(
      summary = "매장 등록 신청 상세 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "관리자 권한이 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_REGISTRATION_NOT_FOUND,
              exampleName = "매장 등록 ID로 매장 등록 신청 내역을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<RegistrationAdmin.DetailViewResponse> getRegistration(
      Long registrationId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "등록 승인", description = "매장 등록 승인 API")
  @ApiResponse(responseCode = "204", description = "매장 등록 승인 성공")
  @ApiErrorResponses(
      summary = "매장 등록 승인 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_APPROVE,
              exampleName = "매장 등록 신청 상태가 신청 또는 재신청이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "관리자 권한이 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_REGISTRATION_NOT_FOUND,
              exampleName = "매장 등록 ID로 매장 등록 신청 내역을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> approve(
      Long registrationId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "등록 거부", description = "매장 등록 거부 API")
  @ApiResponse(responseCode = "204", description = "매장 등록 거부 성공")
  @ApiErrorResponses(
      summary = "매장 등록 거부 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_REJECT,
              exampleName = "매장 등록 신청 상태가 신청 또는 재신청이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "관리자 권한이 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_REGISTRATION_NOT_FOUND,
              exampleName = "매장 등록 ID로 매장 등록 신청 내역을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> reject(
      Long registrationId,
      @RequestBody RegistrationAdminWriteRequest.Reject request,
      @Parameter(hidden = true) Account account
  );

}
