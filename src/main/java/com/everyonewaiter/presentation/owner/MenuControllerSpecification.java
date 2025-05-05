package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.CategoryWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "메뉴")
interface MenuControllerSpecification {

  @Operation(summary = "카테고리 생성", description = "카테고리 생성 API")
  @ApiResponse(responseCode = "201", description = "카테고리 생성 성공")
  @ApiErrorResponses(
      summary = "카테고리 생성 실패 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALREADY_USE_CATEGORY_NAME,
              exampleName = "매장 내에서 이미 사용중인 카테고리 이름인 경우"
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
      }
  )
  ResponseEntity<Void> createCategory(
      Long storeId,
      @RequestBody CategoryWriteRequest.Create request,
      @Parameter(hidden = true) Account account
  );

}
