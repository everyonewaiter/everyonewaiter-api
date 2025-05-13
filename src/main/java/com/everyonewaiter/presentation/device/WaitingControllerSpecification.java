package com.everyonewaiter.presentation.device;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.device.request.WaitingWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "웨이팅")
interface WaitingControllerSpecification {

  @Operation(summary = "웨이팅 등록", description = "웨이팅 등록 API")
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
  ResponseEntity<Void> create(
      @RequestBody WaitingWriteRequest.Create request,
      @Parameter(hidden = true) Device device
  );

}
