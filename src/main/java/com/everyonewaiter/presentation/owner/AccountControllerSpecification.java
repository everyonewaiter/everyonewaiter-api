package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.AccountWrite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "계정")
interface AccountControllerSpecification {

  @SecurityRequirements
  @Operation(
      summary = "계정 생성",
      description = "계정 생성 API<br/><br/>" +
          "휴대폰 번호 인증이 완료된 후 15분 이내 계정 생성을 완료해야 합니다.<br/>" +
          "계정 생성 완료 시 계정의 상태는 **비활성**이며, 이메일 인증을 완료하면 **활성** 상태로 변경됩니다.<br/>" +
          "이메일 인증 확인 메일은 가입 완료 후 자동으로 발송되며, 이메일 인증에 필요한 액세스 토큰은 발송된 메일에 첨부되어 있는 링크에 포함되어 있습니다."
  )
  @ApiResponse(
      responseCode = "201",
      description = "계정 생성 완료",
      headers = @Header(name = "Location", description = "생성된 계정 ID", schema = @Schema(implementation = String.class))
  )
  @ApiErrorResponses(
      summary = "계정 생성 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_USE_EMAIL,
              exampleName = "이미 사용중인 이메일인 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_USE_PHONE_NUMBER,
              exampleName = "이미 사용중인 휴대폰 번호인 경우"
          ),
      }
  )
  ResponseEntity<Void> signUp(@RequestBody AccountWrite.CreateRequest request);

}
