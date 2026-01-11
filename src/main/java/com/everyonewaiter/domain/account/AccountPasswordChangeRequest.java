package com.everyonewaiter.domain.account;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.jspecify.annotations.NonNull;

@Schema(name = "AccountSignInRequest")
public record AccountPasswordChangeRequest(
    @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1", requiredMode = REQUIRED)
    @NotBlank(message = "현재 비밀번호를 확인해 주세요.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
        message = "현재 비밀번호를 확인해 주세요."
    )
    String currentPassword,

    @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1", requiredMode = REQUIRED)
    @NotBlank(message = "새 비밀번호를 확인해 주세요.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
        message = "새 비밀번호를 확인해 주세요."
    )
    String newPassword
) {

  @Override
  public @NonNull String toString() {
    return "AccountPasswordChangeRequest(" +
        "currentPassword='BLIND'" +
        ", newPassword='BLIND'" +
        ')';
  }

}
