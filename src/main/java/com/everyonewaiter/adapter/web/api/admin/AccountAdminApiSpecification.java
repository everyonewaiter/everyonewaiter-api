package com.everyonewaiter.adapter.web.api.admin;

import com.everyonewaiter.adapter.web.api.dto.AccountAdminPageResponse;
import com.everyonewaiter.adapter.web.api.dto.AccountDetailResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponses;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminUpdateRequest;
import com.everyonewaiter.domain.account.AccountSignInRequest;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.Paging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "계정 관리")
interface AccountAdminApiSpecification {

  @Operation(summary = "계정 목록 조회", description = "계정 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "계정 목록 조회 성공")
  @ApiErrorResponses(
      summary = "계정 목록 조회 실패",
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
  ResponseEntity<Paging<AccountAdminPageResponse>> getAccounts(
      @ParameterObject AccountAdminPageRequest readRequest,
      @Parameter(hidden = true) Account account
  );

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
  ResponseEntity<AccountDetailResponse> getAccount(
      Long accountId,
      @Parameter(hidden = true) Account account
  );

  @SecurityRequirements
  @Operation(summary = "로그인", description = "로그인 API")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  @ApiErrorResponses(
      summary = "로그인 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.FAILED_SIGN_IN,
              exampleName = "이메일로 계정을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_SIGN_IN,
              exampleName = "계정이 활성 상태가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_SIGN_IN,
              exampleName = "이메일 및 비밀번호가 일치하지 않는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_SIGN_IN,
              exampleName = "관리자 권한이 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.NOT_COMPLETE_EMAIL_VERIFICATION,
              exampleName = "이메일 인증이 완료되지 않은 경우"
          ),
      }
  )
  ResponseEntity<Void> signIn(@RequestBody AccountSignInRequest signInRequest);

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
      @RequestBody AccountAdminUpdateRequest updateRequest,
      @Parameter(hidden = true) Account account
  );

}
