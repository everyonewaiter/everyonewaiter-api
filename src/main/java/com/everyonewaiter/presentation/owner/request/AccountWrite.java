package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.account.service.request.AccountCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountWrite {

  @Data
  @Schema(name = "AccountWrite.CreateRequest")
  public static class CreateRequest {

    @Schema(description = "이메일", example = "admin@everyonewaiter.com")
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+$", message = "잘못된 형식의 이메일을 입력하셨어요. 이메일을 다시 입력해 주세요.")
    private String email;

    @Schema(description = "비밀번호: 영문, 숫자, 특수문자 조합 8자리 이상", example = "@password1")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=`~])[\\w!@#$%^&*()\\-+=`~]{8,}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 조합하여 8자리 이상으로 입력해 주세요."
    )
    private String password;

    @Schema(description = "휴대폰 번호", example = "01044591812")
    @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
    @Pattern(regexp = "^01[016789]\\d{8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
    private String phoneNumber;

    public AccountCreate toAccountCreate() {
      return new AccountCreate(email, password, phoneNumber);
    }

  }

}
