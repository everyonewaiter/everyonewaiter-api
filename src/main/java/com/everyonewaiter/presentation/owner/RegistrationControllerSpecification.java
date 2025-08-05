package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.store.response.RegistrationResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.RegistrationReadRequest;
import com.everyonewaiter.presentation.owner.request.RegistrationWriteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "매장 등록")
interface RegistrationControllerSpecification {

  @Operation(summary = "등록 신청 목록 조회", description = "매장 등록 신청 목록 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 등록 신청 목록 조회 성공")
  @ApiErrorResponses(
      summary = "매장 등록 신청 목록 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
      }
  )
  ResponseEntity<Paging<RegistrationResponse.Detail>> getRegistrations(
      @ParameterObject RegistrationReadRequest.PageView request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "등록 신청 상세 조회", description = "매장 등록 신청 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "매장 등록 신청 상세 조회 성공")
  @ApiErrorResponses(
      summary = "매장 등록 신청 상세 조회 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_REGISTRATION_NOT_FOUND,
              exampleName = "매장 등록 ID로 매장 등록 신청 내역을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<RegistrationResponse.Detail> getRegistration(
      Long registrationId,
      @Parameter(hidden = true) Account account
  );

  @Operation(
      summary = "등록 신청",
      description = "매장 등록 신청 API<br/><br/>" +
          "사업자 등록증 파일은 이미지 또는 PDF 형식만 지원합니다.<br/>" +
          "PDF 형식의 파일의 경우 첫번째 페이지만 이미지로 변환 후 업로드됩니다."
  )
  @ApiResponse(
      responseCode = "201",
      description = "매장 등록 신청 성공",
      headers = @Header(name = "Location", description = "생성된 매장 등록 ID", schema = @Schema(implementation = String.class))
  )
  @ApiErrorResponses(
      summary = "매장 등록 신청 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ALLOW_IMAGE_AND_PDF_FILE,
              exampleName = "이미지 또는 PDF 형식의 파일이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_CONVERT_PDF_TO_IMAGE,
              exampleName = "PDF 파일을 이미지로 변환하는데 실패한 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_CONVERT_IMAGE_FORMAT,
              exampleName = "이미지 파일을 압축 및 WebP 포맷으로 변환하는데 실패한 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_UPLOAD_IMAGE,
              exampleName = "이미지 업로드 중 오류가 발생한 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
      }
  )
  ResponseEntity<Void> apply(
      RegistrationWriteRequest.Create request,
      @Parameter(hidden = true) Account account
  );

  @Operation(summary = "등록 재신청", description = "매장 등록 재신청 API")
  @ApiResponse(responseCode = "204", description = "매장 등록 재신청 성공")
  @ApiErrorResponses(
      summary = "매장 등록 재신청 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ONLY_REJECTED_REGISTRATION_CAN_BE_REAPPLY,
              exampleName = "거부된 매장 등록 신청이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_REGISTRATION_NOT_FOUND,
              exampleName = "매장 등록 신청 내역을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> reapply(
      Long registrationId,
      @RequestBody RegistrationWriteRequest.Update request,
      @Parameter(hidden = true) Account account
  );

  @Operation(
      summary = "등록 재신청 (이미지 포함)",
      description = "매장 등록 재신청 (이미지 포함) API<br/><br/>" +
          "사업자 등록증 파일은 이미지 또는 PDF 형식만 지원합니다.<br/>" +
          "PDF 형식의 파일의 경우 첫번째 페이지만 이미지로 변환 후 업로드됩니다."
  )
  @ApiResponse(responseCode = "204", description = "매장 등록 재신청 (이미지 포함) 성공")
  @ApiErrorResponses(
      summary = "매장 등록 재신청 (이미지 포함) 실패",
      value = {
          @ApiErrorResponse(
              code = ErrorCode.ONLY_REJECTED_REGISTRATION_CAN_BE_REAPPLY,
              exampleName = "거부된 매장 등록 신청이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.ALLOW_IMAGE_AND_PDF_FILE,
              exampleName = "이미지 또는 PDF 형식의 파일이 아닌 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_CONVERT_PDF_TO_IMAGE,
              exampleName = "PDF 파일을 이미지로 변환하는데 실패한 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_CONVERT_IMAGE_FORMAT,
              exampleName = "이미지 파일을 압축 및 WebP 포맷으로 변환하는데 실패한 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.FAILED_UPLOAD_IMAGE,
              exampleName = "이미지 업로드 중 오류가 발생한 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.UNAUTHORIZED,
              exampleName = "액세스 토큰이 유효하지 않은 경우"
          ),
          @ApiErrorResponse(
              code = ErrorCode.STORE_REGISTRATION_NOT_FOUND,
              exampleName = "매장 등록 신청 내역을 찾을 수 없는 경우"
          ),
      }
  )
  ResponseEntity<Void> reapply(
      Long registrationId,
      RegistrationWriteRequest.UpdateWithImage request,
      @Parameter(hidden = true) Account account
  );

}
