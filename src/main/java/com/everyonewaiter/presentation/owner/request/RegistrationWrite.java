package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.store.request.RegistrationCreate;
import com.everyonewaiter.application.store.request.RegistrationUpdate;
import com.everyonewaiter.application.store.request.RegistrationUpdateWithImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationWrite {

  @Schema(name = "RegistrationWrite.CreateRequest")
  public record CreateRequest(
      @Schema(description = "매장 이름", example = "홍길동식당", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 이름을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "매장 이름은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "대표자명", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "대표자명을 입력해 주세요.")
      @Size(min = 1, max = 20, message = "대표자명은 1자 이상 20자 이하로 입력해 주세요.")
      String ceoName,

      @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 주소를 입력해 주세요.")
      @Size(min = 1, max = 50, message = "매장 주소는 1자 이상 100자 이하로 입력해 주세요.")
      String address,

      @Schema(description = "매장 전화번호", example = "055-123-4567", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 전화번호를 입력해 주세요.")
      @Pattern(
          regexp = "^\\d{2,3}-\\d{3}-\\d{4}$",
          message = "잘못된 형식의 매장 전화번호를 입력하셨어요. 매장 전화번호를 다시 입력해 주세요."
      )
      String landline,

      @Schema(description = "사업자 등록번호", example = "443-60-00875", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "사업자 등록번호를 입력해 주세요.")
      @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "잘못된 형식의 사업자 등록번호를 입력하셨어요. 사업자 등록번호를 다시 입력해 주세요.")
      String license,

      @Schema(description = "사업자 등록증 파일", example = "business_license.pdf")
      @NotNull(message = "사업자 등록증 파일(PDF 또는 이미지)을 선택해 주세요.")
      MultipartFile file
  ) {

    public RegistrationCreate toDomainDto() {
      return new RegistrationCreate(name, ceoName, address, landline, license, file);
    }

  }

  @Schema(name = "RegistrationWrite.UpdateRequest")
  public record UpdateRequest(
      @Schema(description = "매장 이름", example = "홍길동식당")
      @NotBlank(message = "매장 이름을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "매장 이름은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "대표자명", example = "홍길동")
      @NotBlank(message = "대표자명을 입력해 주세요.")
      @Size(min = 1, max = 20, message = "대표자명은 1자 이상 20자 이하로 입력해 주세요.")
      String ceoName,

      @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123")
      @NotBlank(message = "매장 주소를 입력해 주세요.")
      @Size(min = 1, max = 50, message = "매장 주소는 1자 이상 100자 이하로 입력해 주세요.")
      String address,

      @Schema(description = "매장 전화번호", example = "055-123-4567")
      @NotBlank(message = "매장 전화번호를 입력해 주세요.")
      @Pattern(
          regexp = "^\\d{2,3}-\\d{3}-\\d{4}$",
          message = "잘못된 형식의 매장 전화번호를 입력하셨어요. 매장 전화번호를 다시 입력해 주세요."
      )
      String landline,

      @Schema(description = "사업자 등록번호", example = "443-60-00875")
      @NotBlank(message = "사업자 등록번호를 입력해 주세요.")
      @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "잘못된 형식의 사업자 등록번호를 입력하셨어요. 사업자 등록번호를 다시 입력해 주세요.")
      String license
  ) {

    public RegistrationUpdate toDomainDto() {
      return new RegistrationUpdate(name, ceoName, address, landline, license);
    }

  }

  @Schema(name = "RegistrationWrite.UpdateWithImageRequest")
  public record UpdateWithImageRequest(
      @Schema(description = "매장 이름", example = "홍길동식당", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 이름을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "매장 이름은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "대표자명", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "대표자명을 입력해 주세요.")
      @Size(min = 1, max = 20, message = "대표자명은 1자 이상 20자 이하로 입력해 주세요.")
      String ceoName,

      @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 주소를 입력해 주세요.")
      @Size(min = 1, max = 50, message = "매장 주소는 1자 이상 100자 이하로 입력해 주세요.")
      String address,

      @Schema(description = "매장 전화번호", example = "055-123-4567", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 전화번호를 입력해 주세요.")
      @Pattern(
          regexp = "^\\d{2,3}-\\d{3}-\\d{4}$",
          message = "잘못된 형식의 매장 전화번호를 입력하셨어요. 매장 전화번호를 다시 입력해 주세요."
      )
      String landline,

      @Schema(description = "사업자 등록번호", example = "443-60-00875", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "사업자 등록번호를 입력해 주세요.")
      @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "잘못된 형식의 사업자 등록번호를 입력하셨어요. 사업자 등록번호를 다시 입력해 주세요.")
      String license,

      @Schema(description = "사업자 등록증 파일", example = "business_license.pdf")
      @NotNull(message = "사업자 등록증 파일(PDF 또는 이미지)을 선택해 주세요.")
      MultipartFile file
  ) {

    public RegistrationUpdateWithImage toDomainDto() {
      return new RegistrationUpdateWithImage(name, ceoName, address, landline, license, file);
    }

  }

}
