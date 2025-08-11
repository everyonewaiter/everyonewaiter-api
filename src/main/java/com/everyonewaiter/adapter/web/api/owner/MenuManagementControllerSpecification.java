package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.owner.request.MenuWriteRequest;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponse;
import com.everyonewaiter.adapter.web.docs.ApiErrorResponses;
import com.everyonewaiter.application.menu.response.MenuResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.shared.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "메뉴")
interface MenuManagementControllerSpecification {

  @Operation(summary = "메뉴 목록 조회", description = "메뉴 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "메뉴 목록 조회 성공")
  @ApiErrorResponses(
      summary = "메뉴 목록 조회 실패",
      value = {
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
  ResponseEntity<MenuResponse.Simples> getMenus(
      Long storeId,
      Long categoryId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "메뉴 상세 조회", description = "메뉴 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "메뉴 상세 조회 성공")
  @ApiErrorResponses(
      summary = "메뉴 상세 조회 실패",
      value = {
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
              code = ErrorCode.MENU_NOT_FOUND,
              exampleName = "카테고리 ID 및 메뉴 ID로 매장의 메뉴를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<MenuResponse.Detail> getMenu(
      Long storeId,
      Long categoryId,
      Long menuId,
      @Parameter(hidden = true) Account account
  );

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
              code = ErrorCode.INVALID_DISCOUNT_OPTION_PRICE,
              exampleName = "메뉴 할인 옵션 가격이 메뉴 가격보다 큰 경우"
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

  @Operation(summary = "메뉴 수정", description = "메뉴 수정 API")
  @ApiResponse(responseCode = "204", description = "메뉴 수정 성공")
  @ApiErrorResponses(
      summary = "메뉴 수정 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.INVALID_DISCOUNT_OPTION_PRICE,
              exampleName = "메뉴 할인 옵션 가격이 메뉴 가격보다 큰 경우"
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
              code = ErrorCode.MENU_NOT_FOUND,
              exampleName = "메뉴 ID로 매장의 메뉴를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> update(
      Long storeId,
      Long menuId,
      @RequestBody MenuWriteRequest.Update request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "메뉴 수정 (이미지 포함)", description = "메뉴 수정 (이미지 포함) API")
  @ApiResponse(responseCode = "204", description = "메뉴 수정 (이미지 포함) 성공")
  @ApiErrorResponses(
      summary = "메뉴 수정 (이미지 포함) 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.INVALID_DISCOUNT_OPTION_PRICE,
              exampleName = "메뉴 할인 옵션 가격이 메뉴 가격보다 큰 경우"
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
              code = ErrorCode.MENU_NOT_FOUND,
              exampleName = "메뉴 ID로 매장의 메뉴를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> updateWithImage(
      Long storeId,
      Long menuId,
      MultipartFile file,
      MenuWriteRequest.Update request,
      @Parameter(hidden = true) Account account
  );

  @Operation(
      summary = "메뉴 순서 이동",
      description = "메뉴 순서 이동 API<br/><br/>"
          + "- sourceId와 targetId로 메뉴를 찾습니다.<br/>"
          + "- sourceId 메뉴의 위치를 targetId 메뉴의 위치 전,후로 이동합니다.<br/>"
  )
  @ApiResponse(responseCode = "204", description = "메뉴 순서 이동 성공")
  @ApiErrorResponses(
      summary = "메뉴 순서 이동 실패",
      value = {
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
              code = ErrorCode.MENU_NOT_FOUND,
              exampleName = "메뉴 ID로 사장님 매장에 등록된 메뉴를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> movePosition(
      Long storeId,
      Long sourceId,
      Long targetId,
      @RequestBody MenuWriteRequest.MovePosition request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "메뉴 삭제", description = "메뉴 삭제 API")
  @ApiResponse(responseCode = "204", description = "메뉴 삭제 성공")
  @ApiErrorResponses(
      summary = "메뉴 삭제 실패",
      value = {
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
              code = ErrorCode.MENU_NOT_FOUND,
              exampleName = "카테고리 ID 및 메뉴 ID로 매장의 메뉴를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> delete(
      Long storeId,
      Long categoryId,
      Long menuId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "메뉴 다중 삭제", description = "메뉴 다중 삭제 API")
  @ApiResponse(responseCode = "204", description = "메뉴 다중 삭제 성공")
  @ApiErrorResponses(
      summary = "메뉴 다중 삭제 실패",
      value = {
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
              code = ErrorCode.MENU_NOT_FOUND,
              exampleName = "메뉴 ID로 매장의 메뉴를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> deleteAll(
      Long storeId,
      @RequestBody MenuWriteRequest.Delete request,
      @Parameter(hidden = true) Account account
  );

}
