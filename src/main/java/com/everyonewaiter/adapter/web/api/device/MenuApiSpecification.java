package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.domain.menu.CategoryView;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "메뉴")
interface MenuApiSpecification {

  @SecurityRequirements
  @Operation(
      summary = "매장 메뉴 카테고리 및 메뉴 목록 조회",
      description = "매장 메뉴 카테고리 및 메뉴 목록 조회 API<br/><br/>" +
          "숨김(HIDE) 상태의 메뉴는 조회되지 않습니다."
  )
  @ApiResponse(responseCode = "200", description = "매장 메뉴 카테고리 및 메뉴 목록 조회 성공")
  @ApiErrorResponse(
      summary = "매장 메뉴 카테고리 및 메뉴 목록 조회 실패",
      code = ErrorCode.STORE_NOT_FOUND,
      exampleName = "매장을 찾을 수 없는 경우"
  )
  ResponseEntity<CategoryView.CategoryDetails> getStoreMenus(Long storeId);

}
