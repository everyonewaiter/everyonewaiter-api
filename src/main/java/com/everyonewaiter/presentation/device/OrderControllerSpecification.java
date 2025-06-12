package com.everyonewaiter.presentation.device;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.device.request.OrderWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "POS")
interface OrderControllerSpecification {

  @Operation(summary = "[TABLE, POS] 주문 생성", description = "주문 생성 API")
  @ApiResponse(responseCode = "201", description = "주문 생성 성공")
  @ApiErrorResponses(
      summary = "주문 생성 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.NOT_EMPTY_ORDER_MENU,
              exampleName = "주문 메뉴가 비어있는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.INCLUDE_SOLD_OUT_MENU,
              exampleName = "주문 메뉴 중 품절된 메뉴가 포함된 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ORDER_MENU_QUANTITY_POSITIVE,
              exampleName = "주문 메뉴의 수량이 1개 이상이 아닌 경우"
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
              exampleName = "기기의 사용 용도가 TABLE 또는 POS가 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ORDER_MENU_NOT_FOUND,
              exampleName = "주문 메뉴를 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.MENU_OPTION_GROUP_NOT_FOUND,
              exampleName = "주문 메뉴의 옵션 그룹을 찾을 수 없는 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.MENU_OPTION_NOT_FOUND,
              exampleName = "주문 메뉴의 옵션을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> create(
      @RequestBody OrderWriteRequest.Create request,
      @Parameter(hidden = true) Device device
  );

}
