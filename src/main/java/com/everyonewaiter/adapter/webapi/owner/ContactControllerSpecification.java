package com.everyonewaiter.adapter.webapi.owner;

import com.everyonewaiter.adapter.webapi.owner.request.ContactWriteRequest;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "문의")
interface ContactControllerSpecification {

  @SecurityRequirements
  @Operation(summary = "문의하기", description = "문의하기 API")
  @ApiResponse(responseCode = "201", description = "문의하기 성공")
  @ApiErrorResponse(
      summary = "문의하기 실패",
      code = ErrorCode.ALREADY_EXISTS_CONTACT,
      exampleName = "이미 등록된 문의가 있는 경우"
  )
  ResponseEntity<Void> create(@RequestBody ContactWriteRequest.Create request);

}
