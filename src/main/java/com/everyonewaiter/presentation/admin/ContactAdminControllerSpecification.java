package com.everyonewaiter.presentation.admin;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "문의 관리")
interface ContactAdminControllerSpecification {

  @Operation(summary = "문의 완료 처리", description = "문의 완료 처리 API")
  @ApiResponse(responseCode = "204", description = "문의 완료 처리 성공")
  @ApiErrorResponse(
      summary = "문의 완료 처리 실패",
      code = ErrorCode.CONTACT_NOT_FOUND,
      exampleName = "문의를 찾을 수 없는 경우"
  )
  ResponseEntity<Void> create(
      Long contactId,
      @Parameter(hidden = true) Account account
  );

}
