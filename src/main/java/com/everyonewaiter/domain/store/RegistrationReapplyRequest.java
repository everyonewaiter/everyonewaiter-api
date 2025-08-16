package com.everyonewaiter.domain.store;

import static com.everyonewaiter.domain.shared.BusinessLicense.BUSINESS_LICENSE_REGEX;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "RegistrationReapplyRequest")
public record RegistrationReapplyRequest(
    @Schema(description = "매장 이름", example = "홍길동식당", requiredMode = REQUIRED)
    @NotBlank(message = "매장 이름을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "매장 이름은 1자 이상 30자 이하로 입력해 주세요.")
    String name,

    @Schema(description = "대표자명", example = "홍길동", requiredMode = REQUIRED)
    @NotBlank(message = "대표자명을 입력해 주세요.")
    @Size(min = 1, max = 20, message = "대표자명은 1자 이상 20자 이하로 입력해 주세요.")
    String ceoName,

    @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123", requiredMode = REQUIRED)
    @NotBlank(message = "매장 주소를 입력해 주세요.")
    @Size(min = 1, max = 50, message = "매장 주소는 1자 이상 50자 이하로 입력해 주세요.")
    String address,

    @Schema(description = "매장 전화번호", example = "055-123-4567", requiredMode = REQUIRED)
    @NotBlank(message = "매장 전화번호를 입력해 주세요.")
    @Pattern(
        regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
        message = "잘못된 형식의 매장 전화번호를 입력하셨어요. 매장 전화번호를 다시 입력해 주세요."
    )
    String landline,

    @Schema(description = "사업자 등록번호", example = "443-60-00875", requiredMode = REQUIRED)
    @NotBlank(message = "사업자 등록번호를 입력해 주세요.")
    @Pattern(regexp = BUSINESS_LICENSE_REGEX, message = "잘못된 형식의 사업자 등록번호를 입력하셨어요. 사업자 등록번호를 다시 입력해 주세요.")
    String license
) {

}
