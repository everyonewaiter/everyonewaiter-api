package com.everyonewaiter.domain.waiting;

import static com.everyonewaiter.domain.shared.PhoneNumber.PHONE_NUMBER_REGEX;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(name = "WaitingRegisterRequest")
public record WaitingRegisterRequest(
    @Schema(description = "휴대폰 번호", example = "01044591812", requiredMode = REQUIRED)
    @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = "잘못된 형식의 휴대폰 번호를 입력하셨어요. 휴대폰 번호를 다시 입력해 주세요.")
    String phoneNumber,

    @Schema(description = "성인 인원 수", example = "2", requiredMode = REQUIRED)
    @NotNull(message = "성인 인원 수 정보가 누락되었습니다.")
    @Min(value = 1, message = "성인 인원 수는 최소 1 이상으로 입력해 주세요.")
    @Max(value = 100, message = "성인 인원 수는 100 이하로 입력해 주세요.")
    int adult,

    @Schema(description = "유아 인원 수", example = "0", requiredMode = REQUIRED)
    @NotNull(message = "유아 인원 수 정보가 누락되었습니다.")
    @Min(value = 0, message = "유아 인원 수는 최소 0 이상으로 입력해 주세요.")
    @Max(value = 30, message = "유아 인원 수는 30 이하로 입력해 주세요.")
    int infant
) {

}
