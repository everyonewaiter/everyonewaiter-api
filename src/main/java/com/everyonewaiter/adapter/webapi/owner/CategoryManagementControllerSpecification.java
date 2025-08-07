package com.everyonewaiter.adapter.webapi.owner;

import com.everyonewaiter.adapter.webapi.owner.request.CategoryWriteRequest;
import com.everyonewaiter.application.menu.response.CategoryResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "메뉴 카테고리")
interface CategoryManagementControllerSpecification {

  @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
  @ApiErrorResponses(
      summary = "카테고리 목록 조회 실패",
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
  ResponseEntity<CategoryResponse.Simples> getCategories(
      Long storeId,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "카테고리 생성", description = "카테고리 생성 API")
  @ApiResponse(responseCode = "201", description = "카테고리 생성 성공")
  @ApiErrorResponses(
      summary = "카테고리 생성 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.EXCEED_MAXIMUM_CATEGORY_COUNT,
              exampleName = "카테고리 생성 가능 최대 개수(30)를 초과한 경우"
          ),
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
  ResponseEntity<Void> create(
      Long storeId,
      @RequestBody CategoryWriteRequest.Create request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "카테고리 수정", description = "카테고리 수정 API")
  @ApiResponse(responseCode = "204", description = "카테고리 수정 성공")
  @ApiErrorResponses(
      summary = "카테고리 수정 실패",
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
          @ApiErrorResponse(
              code = ErrorCode.CATEGORY_NOT_FOUND,
              exampleName = "카테고리 ID로 사장님 매장에 등록된 카테고리를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> update(
      Long storeId,
      Long categoryId,
      @RequestBody CategoryWriteRequest.Update request,
      @Parameter(hidden = true) Account account
  );

  @Operation(
      summary = "카테고리 순서 이동",
      description = "카테고리 순서 이동 API<br/><br/>"
          + "- sourceId와 targetId로 카테고리를 찾습니다.<br/>"
          + "- sourceId 카테고리의 위치를 targetId 카테고리의 위치 전,후로 이동합니다.<br/>"
  )
  @ApiResponse(responseCode = "204", description = "카테고리 순서 이동 성공")
  @ApiErrorResponses(
      summary = "카테고리 순서 이동 실패",
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
              code = ErrorCode.CATEGORY_NOT_FOUND,
              exampleName = "카테고리 ID로 사장님 매장에 등록된 카테고리를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> movePosition(
      Long storeId,
      Long sourceId,
      Long targetId,
      @RequestBody CategoryWriteRequest.MovePosition request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "카테고리 삭제", description = "카테고리 삭제 API")
  @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공")
  @ApiErrorResponses(
      summary = "카테고리 삭제 실패",
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
              code = ErrorCode.CATEGORY_NOT_FOUND,
              exampleName = "카테고리 ID로 사장님 매장에 등록된 카테고리를 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> delete(
      Long storeId,
      Long categoryId,
      @Parameter(hidden = true) Account account
  );

}
