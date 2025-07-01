package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.device.request.PosWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "POS")
interface PosControllerSpecification {

  @Operation(
      summary = "[POS] 테이블 목록 조회",
      description = "테이블 목록 조회 API<br/><br/>"
          + "응답의 `orderType`, `orderedAt`, `orderMenuName` 필드는 **nullable** 값 입니다.<br/>"
          + "`hasOrder`가 **true**인 경우 해당 필드들은 값이 존재하며, **false**인 경우 null 입니다.<br/><br/>"
          + "**메뉴명 예제**<br/>"
          + "- `orderMenuCount`가 1인 경우: `orderMenuName`<br/>"
          + "- `orderMenuCount`가 2 이상인 경우: `orderMenuName 외 (orderMenuCount-1)개`"
  )
  @ApiResponse(responseCode = "200", description = "테이블 목록 조회 성공")
  @ApiErrorResponses(
      summary = "테이블 목록 조회 실패",
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
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
      }
  )
  ResponseEntity<PosResponse.Tables> getTables(@Parameter(hidden = true) Device device);

  @Operation(summary = "[TABLE, POS] 테이블 액티비티 상세 조회", description = "테이블 액티비티 상세 조회 API")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "테이블 액티비티 상세 조회 성공"),
          @ApiResponse(
              responseCode = "204",
              description = "활성 상태의 테이블 액티비티를 찾지 못한 경우",
              content = @Content(schema = @Schema(type = "null"))
          ),
      }
  )
  @ApiErrorResponses(
      summary = "테이블 액티비티 상세 조회 실패",
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
              code = ErrorCode.POS_TABLE_NOT_FOUND,
              exampleName = "매장 ID와 테이블 번호로 POS 테이블을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<PosResponse.TableActivityDetail> getTableActivity(
      int tableNo,
      @Parameter(hidden = true) Device device
  );

  @Operation(
      summary = "[POS] 테이블 완료",
      description = "테이블 완료 API<br/><br/>"
          + "현재 활성화 되어 있는 테이블 액티비티를 완료 상태로 변경합니다.<br/>"
          + "해당 API는 주로 선결제 테이블에서 사용됩니다.<br/>"
          + "후결제 테이블 또한 테이블 할인을 통해 잔여 결제 금액이 0원인 경우 해당 API를 사용할 수 있습니다."
  )
  @ApiResponse(responseCode = "204", description = "테이블 액티비티 완료 성공")
  @ApiErrorResponses(
      summary = "테이블 액티비티 완료 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.HAS_REMAINING_PAYMENT_PRICE,
              exampleName = "결제할 잔액이 남아있는 경우"
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
              code = ErrorCode.POS_TABLE_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블 액티비티를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> completeActivity(
      int tableNo,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[POS] 테이블 이동", description = "테이블 이동 API")
  @ApiResponse(responseCode = "204", description = "테이블 이동 성공")
  @ApiErrorResponses(
      summary = "테이블 이동 실패",
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
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_NOT_FOUND,
              exampleName = "source 또는 target POS 테이블을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND,
              exampleName = "source 또는 target POS 테이블 액티비티를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> moveTable(
      int sourceTableNo,
      int targetTableNo,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[POS] 테이블 주문 할인", description = "테이블 주문 할인 API")
  @ApiResponse(responseCode = "204", description = "테이블 주문 할인 성공")
  @ApiErrorResponses(
      summary = "테이블 주문 할인 실패",
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
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블 액티비티를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> discount(
      int tableNo,
      @RequestBody PosWriteRequest.Discount request,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[POS] 주문 취소", description = "주문 취소 API")
  @ApiResponse(responseCode = "204", description = "주문 취소 성공")
  @ApiErrorResponses(
      summary = "주문 취소 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.STORE_IS_CLOSED,
              exampleName = "매장이 영업중이지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_CANCELED_ORDER,
              exampleName = "이미 취소된 주문인 경우"
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
              code = ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블 액티비티를 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ORDER_NOT_FOUND,
              exampleName = "주문을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> cancelOrder(
      int tableNo,
      Long orderId,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[POS] 주문 수정", description = "주문 수정 API")
  @ApiResponse(responseCode = "204", description = "주문 수정 성공")
  @ApiErrorResponses(
      summary = "주문 수정 실패",
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
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND,
              exampleName = "POS 테이블 액티비티를 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ORDER_NOT_FOUND,
              exampleName = "주문을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> updateOrders(
      int tableNo,
      @RequestBody PosWriteRequest.UpdateOrders request,
      @Parameter(hidden = true) Device device
  );

  @Operation(summary = "[POS] 주문 메모 수정", description = "주문 메모 수정 API")
  @ApiResponse(responseCode = "204", description = "주문 메모 수정 성공")
  @ApiErrorResponses(
      summary = "주문 메모 수정 실패",
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
              exampleName = "기기의 사용 용도가 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_NOT_FOUND,
              exampleName = "활성화 된 POS 테이블을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND,
              exampleName = "POS 테이블 액티비티를 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ORDER_NOT_FOUND,
              exampleName = "주문을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> updateMemo(
      int tableNo,
      Long orderId,
      @RequestBody PosWriteRequest.UpdateMemo request,
      @Parameter(hidden = true) Device device
  );

}
