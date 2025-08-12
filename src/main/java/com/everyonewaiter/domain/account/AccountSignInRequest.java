package com.everyonewaiter.domain.account;

import static com.everyonewaiter.domain.shared.Email.EMAIL_REGEX;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;

@Schema(name = "AccountSignInRequest")
public record AccountSignInRequest(
    @Schema(description = "이메일", example = "admin@everyonewaiter.com", requiredMode = REQUIRED)
    @NotBlank(message = "이메일 및 비밀번호를 확인해 주세요.")
    @Pattern(regexp = EMAIL_REGEX, message = "이메일 및 비밀번호를 확인해 주세요.")
    String email,

    @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1", requiredMode = REQUIRED)
    @NotBlank(message = "이메일 및 비밀번호를 확인해 주세요.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
        message = "이메일 및 비밀번호를 확인해 주세요."
    )
    String password
) {

  @Override
  public @NonNull String toString() {
    return "AccountSignInRequest(" +
        "email='" + email + '\'' +
        ", password='BLIND'" +
        ')';
  }

}
