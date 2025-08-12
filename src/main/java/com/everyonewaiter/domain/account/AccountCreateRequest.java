package com.everyonewaiter.domain.account;

import static com.everyonewaiter.domain.shared.Email.EMAIL_REGEX;
import static com.everyonewaiter.domain.shared.PhoneNumber.PHONE_NUMBER_REGEX;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;

@Schema(name = "AccountCreateRequest")
public record AccountCreateRequest(
    @Schema(description = "이메일", example = "admin@everyonewaiter.com", requiredMode = REQUIRED)
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = EMAIL_REGEX, message = "잘못된 형식의 이메일을 입력하셨어요. 이메일을 다시 입력해 주세요.")
    String email,

    @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1", requiredMode = REQUIRED)
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 조합하여 8자리 이상으로 입력해 주세요."
    )
    String password,

    @Schema(description = "휴대폰 번호", example = "01044591812", requiredMode = REQUIRED)
    @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
    String phoneNumber
) {

  @Override
  public @NonNull String toString() {
    return "AccountCreateRequest(" +
        "email='" + email + '\'' +
        ", password='BLIND'" +
        ", phoneNumber='" + phoneNumber + '\'' +
        ')';
  }

}
