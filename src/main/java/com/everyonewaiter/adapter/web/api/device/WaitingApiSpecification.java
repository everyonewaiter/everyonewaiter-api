package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.WaitingCountResponse;
import com.everyonewaiter.adapter.web.api.dto.WaitingDetailResponses;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponses;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.waiting.WaitingMyTurnView;
import com.everyonewaiter.domain.waiting.WaitingRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "웨이팅")
interface WaitingApiSpecification {

  @Operation(
      summary = "[HALL] 웨이팅 목록 조회",
      description = "웨이팅 목록 조회 API<br/><br/>"
          + "마지막 손님 호출 시간의 경우 생성할 때 기본으로 UTC 1970-01-01으로 생성하고 있으니, 호출 횟수가 1 이상일 때만 호출 시간을 표시해야 합니다."
  )
  @ApiResponse(responseCode = "200", description = "웨이팅 목록 조회 성공")
  @ApiErrorResponses(
      summary = "웨이팅 목록 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 HALL이 아닌 경우"
          ),
      }
  )
  ResponseEntity<WaitingDetailResponses> getWaitings(@Parameter(hidden = true) Device device);

  @Operation(
      summary = "[WAITING] 웨이팅 수 조회",
      description = "웨이팅 수 조회 API<br/><br/>"
          + "현재 매장에 'REGISTRATION' 상태의 웨이팅 수를 조회합니다."
  )
  @ApiResponse(responseCode = "200", description = "웨이팅 수 조회 성공")
  @ApiErrorResponses(
      summary = "웨이팅 수 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 WAITING이 아닌 경우"
          ),
      }
  )
  ResponseEntity<WaitingCountResponse> count(@Parameter(hidden = true) Device device);

  @SecurityRequirements
  @Operation(
      summary = "내 앞 대기팀 수 조회",
      description = "내 앞 대기팀 수 조회 API<br/><br/>"
          + "손님의 휴대폰으로 전송된 알림톡 내용 중 '내 순서 확인하기' 버튼을 통해 내 앞 대기팀 수 를 조회할 수 있습니다.<br/>"
          + "상태가 취소 또는 완료인 경우 출력할 문구에 대한 분기처리가 필요합니다."
  )
  @ApiResponse(responseCode = "200", description = "내 앞 대기팀 수 조회 성공")
  @ApiErrorResponse(
      summary = "내 앞 대기팀 수 조회 실패",
      code = ErrorCode.WAITING_NOT_FOUND,
      exampleName = "웨이팅 액세스 키로 매장의 웨이팅을 찾을 수 없는 경우"
  )
  ResponseEntity<WaitingMyTurnView> myTurn(Long storeId, String accessKey);

  @Operation(summary = "[WAITING] 웨이팅 등록", description = "웨이팅 등록 API")
  @ApiResponse(
      responseCode = "201",
      description = "웨이팅 등록 성공",
      headers = @Header(name = "Location", description = "생성된 웨이팅 ID", schema = @Schema(implementation = String.class))
  )
  @ApiErrorResponses(
      summary = "웨이팅 등록 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.STORE_IS_CLOSED,
              exampleName = "매장이 영업중이지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_REGISTERED_WAITING,
              exampleName = "이미 웨이팅에 등록되어 있는 휴대폰 번호인 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 WAITING이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "기기에 등록되어 있는 매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> register(
      @RequestBody WaitingRegisterRequest registerRequest,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[HALL] 손님 입장 안내 호출", description = "손님 입장 안내 호출 API")
  @ApiResponse(responseCode = "204", description = "손님 입장 입장 안내 호출 성공")
  @ApiErrorResponses(
      summary = "손님 입장 입장 안내 호출 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.EXCEED_MAXIMUM_CUSTOMER_CALL_COUNT,
              exampleName = "손님 입장 안내 호출 최대 횟수(5)를 초과한 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_CALL,
              exampleName = "웨이팅이 'REGISTRATION' 상태가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 HALL이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.WAITING_NOT_FOUND,
              exampleName = "웨이팅 ID로 매장의 웨이팅을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> call(Long waitingId, @Parameter(hidden = true) Device device);

  @Operation(summary = "[HALL] 손님 입장 완료", description = "손님 입장 완료 API")
  @ApiResponse(responseCode = "204", description = "손님 입장 완료 성공")
  @ApiErrorResponses(
      summary = "손님 입장 완료 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_COMPLETE,
              exampleName = "웨이팅이 'REGISTRATION' 상태가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 HALL이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.WAITING_NOT_FOUND,
              exampleName = "웨이팅 ID로 매장의 웨이팅을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> complete(Long waitingId, @Parameter(hidden = true) Device device);

  @Operation(summary = "[HALL] 웨이팅 취소", description = "웨이팅 취소 API")
  @ApiResponse(responseCode = "204", description = "웨이팅 취소 성공")
  @ApiErrorResponses(
      summary = "웨이팅 취소 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_CANCEL,
              exampleName = "웨이팅이 'REGISTRATION' 상태가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "서명(시그니처)이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 HALL이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.WAITING_NOT_FOUND,
              exampleName = "웨이팅 ID로 매장의 웨이팅을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> cancel(Long waitingId, @Parameter(hidden = true) Device device);

  @SecurityRequirements
  @Operation(summary = "웨이팅 취소", description = "웨이팅 취소 API")
  @ApiResponse(responseCode = "204", description = "웨이팅 취소 성공")
  @ApiErrorResponses(
      summary = "웨이팅 취소 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_CANCEL,
              exampleName = "웨이팅이 'REGISTRATION' 상태가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.WAITING_NOT_FOUND,
              exampleName = "웨이팅 액세스 키로 매장의 웨이팅을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> cancel(Long storeId, String accessKey);

}
