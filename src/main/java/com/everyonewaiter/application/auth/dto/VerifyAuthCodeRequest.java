package com.everyonewaiter.application.auth.dto;

import static com.everyonewaiter.domain.shared.PhoneNumber.PHONE_NUMBER_REGEX;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "VerifyAuthCodeRequest")
public record VerifyAuthCodeRequest(
    @Schema(description = "휴대폰 번호", example = "01044591812", requiredMode = REQUIRED)
    @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
    String phoneNumber,

    @Schema(description = "인증 번호", example = "123456", requiredMode = REQUIRED)
    @Min(value = 100000, message = "인증 번호는 6자리 숫자로 입력해 주세요.")
    @Max(value = 999999, message = "인증 번호는 6자리 숫자로 입력해 주세요.")
    int code
) {

}
