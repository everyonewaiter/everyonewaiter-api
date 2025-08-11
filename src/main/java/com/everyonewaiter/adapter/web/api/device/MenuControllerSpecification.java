package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.application.menu.response.CategoryResponse;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "메뉴")
interface MenuControllerSpecification {

  @SecurityRequirements
  @Operation(summary = "매장 메뉴 카테고리 및 메뉴 목록 조회", description = "매장 메뉴 카테고리 및 메뉴 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 메뉴 카테고리 및 메뉴 목록 조회 성공")
  @ApiErrorResponse(
      summary = "매장 메뉴 카테고리 및 메뉴 목록 조회 실패",
      code = ErrorCode.STORE_NOT_FOUND,
      exampleName = "매장을 찾을 수 없는 경우"
  )
  ResponseEntity<CategoryResponse.All> getStoreMenus(Long storeId);

}
