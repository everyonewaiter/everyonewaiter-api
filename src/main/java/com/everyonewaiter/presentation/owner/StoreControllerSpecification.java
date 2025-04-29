package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.ApiErrorResponse;
import com.everyonewaiter.global.annotation.ApiErrorResponses;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.presentation.owner.request.StoreWrite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "매장")
interface StoreControllerSpecification {

  @Operation(
      summary = "등록 신청",
      description = "매장 등록 신청 API<br/><br/>" +
          "사업자 등록증 파일은 이미지 또는 PDF 형식만 지원합니다.<br/>" +
          "PDF 형식의 파일의 경우 첫번째 페이지만 이미지로 변환 후 업로드됩니다."
  )
  @ApiResponse(responseCode = "201", description = "매장 등록 신청 성공")
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
      StoreWrite.RegistrationCreateRequest request,
      @Parameter(hidden = true) Account account
  );

}
