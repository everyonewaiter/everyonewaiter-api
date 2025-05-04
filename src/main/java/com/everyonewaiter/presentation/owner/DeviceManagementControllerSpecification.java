package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.support.Paging;
import com.everyonewaiter.presentation.owner.request.AuthWriteRequest;
import com.everyonewaiter.presentation.owner.request.DeviceReadRequest;
import com.everyonewaiter.presentation.owner.request.DeviceWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "기기")
interface DeviceManagementControllerSpecification {

  @Operation(summary = "기기 목록 조회", description = "기기 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "기기 목록 조회 성공")
  @ApiErrorResponses(
      summary = "기기 목록 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "사장님 권한이 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장 ID로 사장님 소유의 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Paging<DeviceResponse.PageView>> getDevices(
      Long storeId,
      @ParameterObject DeviceReadRequest.PageView request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "기기 상세 조회", description = "기기 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "기기 상세 조회 성공")
  @ApiErrorResponses(
      summary = "기기 상세 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "사장님 권한이 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장 ID로 사장님 소유의 매장을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.DEVICE_NOT_FOUND,
              exampleName = "기기 ID로 사장님 매장에 등록된 기기를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<DeviceResponse.Detail> getDevice(
      Long storeId,
      Long deviceId,
      @Parameter(hidden = true) Account account
  );

  @SecurityRequirements
  @Operation(
      summary = "기기 생성",
      description = "기기 생성 API<br/><br/>" +
          "기기의 사용 용도에 따라 추가적으로 유효성 검사를 진행합니다.<br/>" +
          "- **POS**: KSNET 단말기 번호 8자 이상 30자 이하<br/>" +
          "- **TABLE**: 결제 타입이 선결제인 경우 KSNET 단말기 번호 8자리 이상 30자 이하, 테이블 번호 1 이상<br/><br/>" +
          "기기 생성 시 사용 용도에 따라 요청 본문과 상관없이 기본값을 사용합니다.<br/>" +
          "- **POS**: 테이블 번호 0, 결제 타입 POSTPAID<br/>" +
          "- **HALL**, **WAITING**: 테이블 번호 0, 결제 타입 POSTPAID, KSNET 단말기 번호 빈 문자열"
  )
  @ApiResponse(
      responseCode = "201",
      description = "기기 생성 완료",
      headers = @Header(name = "Location", description = "생성된 기기 ID", schema = @Schema(implementation = String.class))
  )
  @ApiErrorResponses(
      summary = "기기 생성 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.EXPIRED_VERIFICATION_PHONE_NUMBER,
              exampleName = "휴대폰 번호 인증이 만료된 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_USE_DEVICE_NAME,
              exampleName = "매장 내에서 이미 사용 중인 기기 이름인 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ACCOUNT_NOT_FOUND,
              exampleName = "휴대폰 번호로 계정을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장 ID로 사장님 소유의 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<DeviceResponse.Create> create(
      Long storeId,
      @RequestBody DeviceWriteRequest.Create request
  );

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
              code = ErrorCode.EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER,
              exampleName = "일일 인증 횟수가 초과된 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ACCOUNT_NOT_FOUND,
              exampleName = "휴대폰 번호로 계정을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> sendAuthCode(@RequestBody AuthWriteRequest.SendAuthCode request);

  @SecurityRequirements
  @Operation(
      summary = "휴대폰 인증",
      description = "휴대폰 번호 인증 API<br/><br/>" +
          "휴대폰 번호 인증이 완료된 후 30분 이내 기기 생성을 완료해야 합니다."
  )
  @ApiResponse(responseCode = "200", description = "휴대폰 번호 인증 성공")
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
          @ApiErrorResponse(
              code = ErrorCode.ACCOUNT_NOT_FOUND,
              exampleName = "휴대폰 번호로 계정을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<StoreResponse.Simples> verifyAuthCode(
      @RequestBody AuthWriteRequest.VerifyAuthCode request
  );

  @Operation(summary = "기기 정보 수정", description = "기기 정보 수정 API")
  @ApiResponse(responseCode = "204", description = "기기 정보 수정 성공")
  @ApiErrorResponses(
      summary = "기기 정보 수정 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_USE_DEVICE_NAME,
              exampleName = "매장 내에서 이미 사용 중인 기기 이름인 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "사장님 권한이 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장 ID로 사장님 소유의 매장을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.DEVICE_NOT_FOUND,
              exampleName = "기기 ID로 사장님 매장에 등록된 기기를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> update(
      Long storeId,
      Long deviceId,
      @RequestBody DeviceWriteRequest.Update request,
      @Parameter(hidden = true) Account account
  );

}
