package com.everyonewaiter.adapter.webapi.admin;

import com.everyonewaiter.adapter.webapi.admin.request.ContactAdminReadRequest;
import com.everyonewaiter.application.contact.response.ContactAdminResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "문의 관리")
interface ContactAdminControllerSpecification {

  @Operation(summary = "문의 목록 조회", description = "문의 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "문의 목록 조회 성공")
  @ApiErrorResponses(
      summary = "문의 목록 조회 실패",
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
  ResponseEntity<Paging<ContactAdminResponse.PageView>> getContacts(
      @ParameterObject ContactAdminReadRequest.PageView request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "문의 완료 처리", description = "문의 완료 처리 API")
  @ApiResponse(responseCode = "204", description = "문의 완료 처리 성공")
  @ApiErrorResponses(
      summary = "문의 완료 처리 실패",
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
              code = ErrorCode.CONTACT_NOT_FOUND,
              exampleName = "문의를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> create(
      Long contactId,
      @Parameter(hidden = true) Account account
  );

}
