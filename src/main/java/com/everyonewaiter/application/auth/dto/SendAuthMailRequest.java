package com.everyonewaiter.application.auth.dto;

import static com.everyonewaiter.domain.shared.Email.EMAIL_REGEX;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "SendAuthMailRequest")
public record SendAuthMailRequest(
    @Schema(description = "이메일", example = "admin@everyonewaiter.com", requiredMode = REQUIRED)
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = EMAIL_REGEX, message = "잘못된 형식의 이메일을 입력하셨어요. 이메일을 다시 입력해 주세요.")
    String email
) {

}
