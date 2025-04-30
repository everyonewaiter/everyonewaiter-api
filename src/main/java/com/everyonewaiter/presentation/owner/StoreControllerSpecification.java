package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.store.response.StoreSimpleResponses;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "매장")
interface StoreControllerSpecification {

  @Operation(summary = "매장 목록 조회", description = "매장 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 목록 조회 성공")
  @ApiErrorResponse(
      summary = "매장 목록 조회 실패",
      code = ErrorCode.UNAUTHORIZED,
      exampleName = "액세스 토큰이 유효하지 않은 경우"
  )
  ResponseEntity<StoreSimpleResponses> getStores(@Parameter(hidden = true) Account account);
  ResponseEntity<StoreResponse.Simples> getStores(@Parameter(hidden = true) Account account);


}
