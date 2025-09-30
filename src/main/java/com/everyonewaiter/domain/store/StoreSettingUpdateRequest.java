package com.everyonewaiter.domain.store;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "StoreSettingUpdateRequest")
public record StoreSettingUpdateRequest(
    @Schema(description = "KSNET 단말기 번호", example = "DPTOTEST03", requiredMode = REQUIRED)
    @NotNull(message = "KSNET 단말기 번호가 누락되었습니다.")
    @Size(min = 8, max = 30, message = "KSNET 단말기 번호는 8자 이상 30자 이하로 입력해 주세요.")
    String ksnetDeviceNo,

    @Schema(description = "POS 여분 테이블 표시 수", example = "5", requiredMode = REQUIRED)
    @NotNull(message = "POS 여분 테이블 표시 수가 누락되었습니다.")
    @Min(value = 0, message = "여분 테이블 수는 0 이상 10 이하로 입력해 주세요.")
    @Max(value = 10, message = "여분 테이블 수는 0 이상 10 이하로 입력해 주세요.")
    Integer extraTableCount,

    @Schema(description = "주방 프린터와 연결된 기기 위치", example = "POS", requiredMode = REQUIRED)
    @NotNull(message = "주방 프린터와 연결된 기기 위치가 누락되었습니다.")
    PrinterLocation printerLocation,

    @Schema(description = "손님 테이블 메뉴 팝업 보이기 여부", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "손님 테이블 메뉴 팝업 보이기 여부가 누락되었습니다.")
    Boolean showMenuPopup,

    @Schema(description = "손님 테이블 총 주문 금액 표시 여부", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "손님 테이블 총 주문 금액 표시 여부가 누락되었습니다.")
    Boolean showOrderTotalPrice,

    @Schema(description = "홀 관리 주문 메뉴 이미지 표시 여부", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "홀 관리 주문 메뉴 이미지 표시 여부가 누락되었습니다.")
    Boolean showOrderMenuImage,

    @Schema(description = "원산지 정보", requiredMode = REQUIRED)
    @NotNull(message = "원산지 정보가 누락되었습니다.")
    @Size(max = 20, message = "원산지 정보는 최대 20개까지 등록할 수 있어요.")
    List<@Valid CountryOfOrigin> countryOfOrigins,

    @Schema(description = "직원 호출 옵션", example = "[\"직원 호출\", \"포장\"]", requiredMode = REQUIRED)
    @NotNull(message = "직원 호출 옵션이 누락되었습니다.")
    @Size(max = 12, message = "직원 호출 옵션은 최대 12개까지 등록할 수 있어요.")
    List<@Size(min = 1, max = 10, message = "직원 호출 옵션은 1자 이상 10자 이하로 입력해 주세요.") String> staffCallOptions
) {

}
