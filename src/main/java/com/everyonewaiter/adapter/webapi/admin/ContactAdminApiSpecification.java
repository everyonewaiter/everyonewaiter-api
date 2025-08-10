package com.everyonewaiter.adapter.webapi.admin;

import com.everyonewaiter.adapter.webapi.admin.dto.ContactAdminReadResponse;
import com.everyonewaiter.application.contact.dto.ContactAdminReadRequest;
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

@Tag(name = "서비스 도입 문의")
interface ContactAdminApiSpecification {

  @Operation(summary = "서비스 도입 목록 조회", description = "서비스 도입 문의 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "서비스 도입 문의 목록 조회 성공")
  @ApiErrorResponses(
      summary = "서비스 도입 문의 목록 조회 실패",
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
  ResponseEntity<Paging<ContactAdminReadResponse>> getContacts(
      @ParameterObject ContactAdminReadRequest request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "서비스 도입 문의 처리중 상태 변경", description = "서비스 도입 문의 처리중 상태 변경 API")
  @ApiResponse(responseCode = "204", description = "서비스 도입 문의 처리중 상태 변경 성공")
  @ApiErrorResponses(
      summary = "서비스 도입 문의 처리중 상태 변경 실패",
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
  ResponseEntity<Void> processing(
      Long contactId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "서비스 도입 문의 처리 완료 상태 변경", description = "서비스 도입 문의 처리 완료 상태 변경 API")
  @ApiResponse(responseCode = "204", description = "서비스 도입 문의 처리 완료 상태 변경 성공")
  @ApiErrorResponses(
      summary = "서비스 도입 문의 처리 완료 상태 변경 실패",
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
  ResponseEntity<Void> complete(
      Long contactId,
      @Parameter(hidden = true) Account account
  );

}
