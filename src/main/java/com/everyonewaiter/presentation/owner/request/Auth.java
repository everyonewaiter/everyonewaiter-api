package com.everyonewaiter.presentation.owner.request;

import com.everyonewaiter.application.auth.service.request.SendAuthCode;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Auth {

  @Schema(name = "Auth.SendAuthCodeRequest")
  public record SendAuthCodeRequest(
      @Schema(description = "휴대폰 번호", example = "01044591812")
      @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
      @Pattern(regexp = "^01[016789]\\d{8}$", message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
      String phoneNumber
  ) {

    public SendAuthCode toSendAuthCode(AuthPurpose purpose) {
      return new SendAuthCode(phoneNumber, purpose);
    }

  }

}
