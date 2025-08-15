package com.everyonewaiter.domain.auth;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

@Schema(name = "SignInTokenRenewRequest")
public record SignInTokenRenewRequest(
    @Schema(description = "리프레시 토큰", example = "abcdefghijklmnopqrstuvwxyz", requiredMode = REQUIRED)
    @NotBlank(message = "리프레시 토큰을 입력해 주세요.")
    String refreshToken
) {

  @Override
  public @NonNull String toString() {
    return "SignInTokenRenewRequest(" +
        "refreshToken='BLIND'" +
        ')';
  }

}
