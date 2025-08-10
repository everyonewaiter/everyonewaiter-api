package com.everyonewaiter.adapter.webapi.common;

import com.everyonewaiter.domain.contact.ContactCreateRequest;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "서비스 도입 문의")
interface ContactApiSpecification {

  @SecurityRequirements
  @Operation(summary = "서비스 도입 문의", description = "서비스 도입 문의 API")
  @ApiResponse(responseCode = "201", description = "서비스 도입 문의 성공")
  @ApiErrorResponse(
      summary = "서비스 도입 문의 실패",
      code = ErrorCode.ALREADY_EXISTS_CONTACT,
      exampleName = "이미 등록된 문의가 있는 경우"
  )
  ResponseEntity<Void> create(@RequestBody ContactCreateRequest request);

}
