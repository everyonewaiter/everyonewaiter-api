package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.MenuWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "메뉴")
interface MenuControllerSpecification {

  @Operation(summary = "메뉴 생성", description = "메뉴 생성 API")
  @ApiResponse(responseCode = "201", description = "메뉴 생성 성공")
  @ApiErrorResponses(
      summary = "메뉴 생성 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.EXCEED_MAXIMUM_MENU_COUNT,
              exampleName = "카테고리당 메뉴 생성 가능 최대 개수(50)를 초과한 경우"
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
              code = ErrorCode.CATEGORY_NOT_FOUND,
              exampleName = "카테고리 ID로 매장의 카테고리를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> create(
      Long storeId,
      Long categoryId,
      MultipartFile file,
      MenuWriteRequest.Create request,
      @Parameter(hidden = true) Account account
  );

}
