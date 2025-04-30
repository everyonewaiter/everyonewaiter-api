package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.auth.request.AuthWrite;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthWriteRequest {

  @Schema(name = "AuthWriteRequest.SendAuthCode")
  public record SendAuthCode(
      @Schema(description = "휴대폰 번호", example = "01044591812")
      @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
      @Pattern(regexp = "^01[016789]\\d{8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
      String phoneNumber
  ) {

    public AuthWrite.SendAuthCode toDomainDto(AuthPurpose purpose) {
      return new AuthWrite.SendAuthCode(phoneNumber, purpose);
    }

  }

  @Schema(name = "AuthWriteRequest.VerifyAuthCode")
  public record VerifyAuthCode(
      @Schema(description = "휴대폰 번호", example = "01044591812")
      @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
      @Pattern(regexp = "^01[016789]\\d{8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
      String phoneNumber,

      @Schema(description = "인증 번호", example = "123456")
      @Min(value = 100000, message = "인증 번호는 6자리 숫자로 입력해 주세요.")
      @Max(value = 999999, message = "인증 번호는 6자리 숫자로 입력해 주세요.")
      int code
  ) {

    public AuthWrite.VerifyAuthCode toDomainDto(AuthPurpose purpose) {
      return new AuthWrite.VerifyAuthCode(phoneNumber, code, purpose);
    }

  }

  @Schema(name = "AuthWriteRequest.SendAuthMail")
  public record SendAuthMail(
      @Schema(description = "이메일", example = "admin@everyonewaiter.com")
      @NotBlank(message = "이메일을 입력해 주세요.")
      @Pattern(regexp = "^[\\w+-.*]+@[\\w-]+\\.[\\w-.]+$", message = "잘못된 형식의 이메일을 입력하셨어요. 이메일을 다시 입력해 주세요.")
      String email
  ) {

  }

  @Schema(name = "AuthWriteRequest.RenewToken")
  public record RenewToken(
      @Schema(description = "리프레시 토큰", example = "abcdefghijklmnopqrstuvwxyz")
      @NotBlank(message = "리프레시 토큰을 입력해 주세요.")
      String refreshToken
  ) {

  }

}
