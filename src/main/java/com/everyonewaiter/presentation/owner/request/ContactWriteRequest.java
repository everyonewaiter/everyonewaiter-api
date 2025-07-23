package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.contact.request.ContactWrite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactWriteRequest {

  @Schema(name = "ContactWriteRequest.Create")
  public record Create(
      @Schema(description = "매장 이름", example = "홍길동식당", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "매장 이름을 입력해 주세요.")
      @Size(min = 1, max = 30, message = "매장 이름은 1자 이상 30자 이하로 입력해 주세요.")
      String name,

      @Schema(description = "휴대폰 번호", example = "01044591812", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
      @Pattern(regexp = "^01[016789]\\d{8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
      String phoneNumber,

      @Schema(description = "사업자 등록번호", example = "443-60-00875", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "사업자 등록번호를 입력해 주세요.")
      @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "잘못된 형식의 사업자 등록번호를 입력하셨어요. 사업자 등록번호를 다시 입력해 주세요.")
      String license
  ) {

    public ContactWrite.Create toDomainDto() {
      return new ContactWrite.Create(name, phoneNumber, license);
    }

  }

}
