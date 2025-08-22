package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.OrderPaymentDetailResponses;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponses;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.order.OrderPaymentApproveRequest;
import com.everyonewaiter.domain.order.OrderPaymentCancelRequest;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "주문 결제")
interface OrderPaymentApiSpecification {

  @Operation(
      summary = "[POS] 결제 내역 조회",
      description = "결제 내역 조회 API<br/><br/>"
          + "날짜 포맷은 KST `yyyyMMdd` 형식이어야 합니다. 예시: `20250101`"
  )
  @ApiResponse(responseCode = "200", description = "결제 내역 조회 성공")
  @ApiErrorResponses(
      summary = "결제 내역 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.INVALID_DATE_FORMAT,
              exampleName = "날짜 포맷이 올바르지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "인증 시그니처가 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
      }
  )
  ResponseEntity<OrderPaymentDetailResponses> getOrderPaymentsByPos(
      String date,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[TABLE, POS] 주문 결제 승인", description = "주문 결제 승인 API")
  @ApiResponse(
      responseCode = "201",
      description = "주문 결제 승인 성공",
      headers = @Header(name = "Location", description = "생성된 주문 결제 승인 ID", schema = @Schema(implementation = String.class))
  )
  @ApiErrorResponses(
      summary = "주문 결제 승인 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.STORE_IS_CLOSED,
              exampleName = "매장이 영업중이지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "인증 시그니처가 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 TABLE 또는 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블 액티비티를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> approve(
      int tableNo,
      @RequestBody OrderPaymentApproveRequest approveRequest,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[POS] 주문 결제 취소", description = "주문 결제 취소 API")
  @ApiResponse(
      responseCode = "201",
      description = "주문 결제 취소 성공",
      headers = @Header(name = "Location", description = "생성된 주문 결제 취소 ID", schema = @Schema(implementation = String.class))
  )
  @ApiErrorResponses(
      summary = "주문 결제 취소 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_CANCELED_ORDER_PAYMENT,
              exampleName = "이미 취소된 결제 내역인 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_IS_CLOSED,
              exampleName = "매장이 영업중이지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "인증 시그니처가 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FORBIDDEN,
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "매장을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ORDER_PAYMENT_NOT_FOUND,
              exampleName = "주문 결제 내역을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> cancel(
      Long orderPaymentId,
      @RequestBody OrderPaymentCancelRequest cancelRequest,
      @Parameter(hidden = true) Device device
  );

}
