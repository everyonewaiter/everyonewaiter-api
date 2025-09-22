package com.everyonewaiter.adapter.web.api.admin;

import com.everyonewaiter.adapter.web.api.dto.ContactAdminPageResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponses;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.contact.ContactAdminPageRequest;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.Paging;
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
  ResponseEntity<Paging<ContactAdminPageResponse>> getContacts(
      @ParameterObject ContactAdminPageRequest request,
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
