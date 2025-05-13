package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.StoreWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "매장")
interface StoreManagementControllerSpecification {

  @Operation(summary = "매장 목록 조회", description = "매장 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 목록 조회 성공")
  @ApiErrorResponse(
      summary = "매장 목록 조회 실패",
      code = ErrorCode.UNAUTHORIZED,
      exampleName = "액세스 토큰이 유효하지 않은 경우"
  )
  ResponseEntity<StoreResponse.Simples> getStores(@Parameter(hidden = true) Account account);

  @Operation(summary = "매장 상세 조회", description = "매장 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 상세 조회 성공")
  @ApiErrorResponses(
      summary = "매장 상세 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "계정 ID 및 매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<StoreResponse.Detail> getStore(
      Long storeId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "매장 정보 수정", description = "매장 정보 수정 API")
  @ApiResponse(responseCode = "204", description = "매장 정보 수정 성공")
  @ApiErrorResponses(
      summary = "매장 정보 수정 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_NOT_FOUND,
              exampleName = "계정 ID 및 매장 ID로 매장을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> update(
      Long storeId,
      @RequestBody StoreWriteRequest.Update request,
      @Parameter(hidden = true) Account account
  );

}
