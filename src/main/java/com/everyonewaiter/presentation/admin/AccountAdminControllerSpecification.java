package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.application.account.service.response.AccountAdmin;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.admin.request.AccountAdminWrite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "계정 관리")
interface AccountAdminControllerSpecification {

  @Operation(summary = "계정 상세 조회", description = "계정 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "계정 상세 조회 성공")
  @ApiErrorResponses(
      summary = "계정 상세 조회 실패",
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
              code = ErrorCode.ACCOUNT_NOT_FOUND,
              exampleName = "계정 ID로 계정을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<AccountAdmin.ReadResponse> getAccount(
      Long accountId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "계정 정보 수정", description = "계정 정보 수정 API")
  @ApiResponse(responseCode = "204", description = "계정 정보 수정 성공")
  @ApiErrorResponses(
      summary = "계정 정보 수정 실패",
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
              code = ErrorCode.ACCOUNT_NOT_FOUND,
              exampleName = "계정 ID로 계정을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> update(
      Long accountId,
      @RequestBody AccountAdminWrite.UpdateRequest request,
      @Parameter(hidden = true) Account account
  );

}
