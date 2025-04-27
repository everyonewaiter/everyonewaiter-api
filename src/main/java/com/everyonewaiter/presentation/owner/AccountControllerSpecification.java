package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.AccountWrite;
import com.everyonewaiter.presentation.owner.request.Auth;
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
          @ApiErrorResponse(
              code = ErrorCode.EXPIRED_VERIFICATION_PHONE_NUMBER,
              exampleName = "휴대폰 번호 인증이 만료된 경우"
          ),
      }
  )
  ResponseEntity<Void> signUp(@RequestBody AccountWrite.CreateRequest request);

  @SecurityRequirements
  @Operation(
      summary = "휴대폰 인증 번호 알림톡 발송",
      description = "휴대폰 인증 번호 알림톡 발송 요청 API<br/><br/>" +
          "24시간동안 최대 5번까지 요청할 수 있습니다.<br/>" +
          "**5분**의 유효기간을 가진 6자리의 랜덤 번호를 생성 후, 요청 본문의 휴대폰 번호로 알림톡을 발송합니다."
  )
  @ApiResponse(responseCode = "204", description = "휴대폰 인증 번호 알림톡 발송 요청 성공")
  @ApiErrorResponses(
      summary = "휴대폰 인증 번호 알림톡 발송 요청 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_USE_PHONE_NUMBER,
              exampleName = "이미 사용중인 휴대폰 번호인 경우"
          ),

          @ApiErrorResponse(
              code = ErrorCode.EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER,
              exampleName = "일일 인증 횟수가 초과된 경우"
          ),
      }
  )
  ResponseEntity<Void> sendAuthCode(@RequestBody Auth.SendAuthCodeRequest request);

  @SecurityRequirements
  @Operation(
      summary = "휴대폰 인증",
      description = "휴대폰 번호 인증 API<br/><br/>" +
          "휴대폰 번호 인증이 완료된 후 15분 이내 계정 생성을 완료해야 합니다."
  )
  @ApiResponse(responseCode = "204", description = "휴대폰 번호 인증 성공")
  @ApiErrorResponses(
      summary = "휴대폰 번호 인증 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_VERIFIED_PHONE_NUMBER,
              exampleName = "이미 휴대폰 번호 인증이 완료된 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.EXPIRED_VERIFICATION_CODE,
              exampleName = "인증 번호가 만료된 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNMATCHED_VERIFICATION_CODE,
              exampleName = "인증 번호가 일치하지 않는 경우"
          ),
      }
  )
  ResponseEntity<Void> verifyAuthCode(@RequestBody Auth.VerifyAuthCodeRequest request);

  @SecurityRequirements
  @Operation(
      summary = "이메일 인증 확인 메일 발송",
      description = "이메일 인증 확인 메일 발송 요청 API<br/><br/>" +
          "계정 생성 시 자동으로 발송됨으로 계정 생성 요청 후 직접 해당 API를 호출할 필요가 없습니다.<br/>" +
          "이메일 인증 확인 메일 내 첨부된 토큰이 만료된 경우 해당 API를 이용하여 확인 메일을 재발송할 수 있습니다."
  )
  @ApiResponse(responseCode = "204", description = "이메일 인증 확인 메일 발송 요청 성공")
  @ApiErrorResponses(
      summary = "이메일 인증 확인 메일 발송 요청 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_VERIFIED_EMAIL,
              exampleName = "이미 이메일 인증이 완료된 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ACCOUNT_NOT_FOUND,
              exampleName = "이메일로 회원을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> sendAuthMail(@RequestBody Auth.SendAuthMailRequest request);

}
