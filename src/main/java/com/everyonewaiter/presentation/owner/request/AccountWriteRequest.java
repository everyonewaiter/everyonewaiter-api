package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.account.request.AccountWrite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountWriteRequest {

  @Schema(name = "AccountWriteRequest.Create")
  public record Create(
      @Schema(description = "이메일", example = "admin@everyonewaiter.com", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "이메일을 입력해 주세요.")
      @Pattern(regexp = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+$", message = "잘못된 형식의 이메일을 입력하셨어요. 이메일을 다시 입력해 주세요.")
      String email,

      @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "비밀번호를 입력해 주세요.")
      @Pattern(
          regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
          message = "비밀번호는 영문, 숫자, 특수문자를 조합하여 8자리 이상으로 입력해 주세요."
      )
      String password,

      @Schema(description = "휴대폰 번호", example = "01044591812", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
      @Pattern(regexp = "^01[016789]\\d{8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
      String phoneNumber
  ) {

    public AccountWrite.Create toDomainDto() {
      return new AccountWrite.Create(email, password, phoneNumber);
    }

    @Override
    public @NonNull String toString() {
      return "Create(" +
          "email='" + email + '\'' +
          ", password='BLIND'" +
          ", phoneNumber='" + phoneNumber + '\'' +
          ')';
    }

  }

  @Schema(name = "AccountWriteRequest.SignIn")
  public record SignIn(
      @Schema(description = "이메일", example = "admin@everyonewaiter.com", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "이메일 및 비밀번호를 확인해 주세요.")
      @Pattern(regexp = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+$", message = "이메일 및 비밀번호를 확인해 주세요.")
      String email,

      @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1", requiredMode = Schema.RequiredMode.REQUIRED)
      @NotBlank(message = "이메일 및 비밀번호를 확인해 주세요.")
      @Pattern(
          regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
          message = "이메일 및 비밀번호를 확인해 주세요."
      )
      String password
  ) {

    public AccountWrite.SignIn toDomainDto() {
      return new AccountWrite.SignIn(email, password);
    }

    @Override
    public @NonNull String toString() {
      return "SignIn(" +
          "email='" + email + '\'' +
          ", password='BLIND'" +
          ')';
    }

  }

}
