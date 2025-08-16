package com.everyonewaiter.domain.device;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "DeviceUpdateRequest")
public record DeviceUpdateRequest(
    @Schema(description = "기기 이름", example = "1번 테이블", requiredMode = REQUIRED)
    @NotBlank(message = "기기 이름을 입력해 주세요.")
    @Size(min = 1, max = 20, message = "기기 이름은 1자 이상 20자 이하로 입력해 주세요.")
    String name,

    @Schema(description = "기기 사용 용도", example = "TABLE", requiredMode = REQUIRED)
    @NotNull(message = "기기 사용 용도가 누락되었습니다.")
    DevicePurpose purpose,

    @Schema(description = "테이블 번호", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "테이블 번호가 누락되었습니다.")
    @Min(value = 0, message = "테이블 번호는 0 이상으로 입력해 주세요.")
    @Max(value = 100, message = "테이블 번호는 100 이하로 입력해 주세요.")
    Integer tableNo,

    @Schema(description = "결제 타입 (선결제, 후결제)", example = "POSTPAID", requiredMode = REQUIRED)
    @NotNull(message = "결제 타입이 누락되었습니다.")
    DevicePaymentType paymentType
) {

}
