package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.store.CountryOfOrigin;
import com.everyonewaiter.domain.store.PrinterLocation;
import com.everyonewaiter.domain.store.Setting;
import com.everyonewaiter.domain.store.StaffCallOption;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "StoreSettingDetailResponse")
public record StoreSettingDetailResponse(
    @Schema(description = "KSNET 단말기 번호", example = "DPTOTEST01")
    String ksnetDeviceNo,

    @Schema(description = "POS 여분 테이블 표시 수", example = "5")
    int extraTableCount,

    @Schema(description = "주방 프린터와 연결된 기기 위치", example = "POS")
    PrinterLocation printerLocation,

    @Schema(description = "손님 테이블 메뉴 팝업 보이기 여부", example = "true")
    boolean showMenuPopup,

    @Schema(description = "손님 테이블 총 주문 금액 표시 여부", example = "true")
    boolean showOrderTotalPrice,

    @Schema(description = "홀 관리 주문 메뉴 이미지 표시 여부", example = "true")
    boolean showOrderMenuImage,

    @Schema(description = "원산지 정보")
    List<CountryOfOrigin> countryOfOrigins,

    @Schema(description = "직원 호출 옵션", example = "[\"직원 호출\", \"포장\"]")
    List<String> staffCallOptions
) {

  public static StoreSettingDetailResponse from(Setting setting) {
    return new StoreSettingDetailResponse(
        setting.getKsnetDeviceNo(),
        setting.getExtraTableCount(),
        setting.getPrinterLocation(),
        setting.isShowMenuPopup(),
        setting.isShowOrderTotalPrice(),
        setting.isShowOrderMenuImage(),
        setting.getCountryOfOrigins(),
        setting.getStaffCallOptions().stream().map(StaffCallOption::optionName).toList()
    );
  }

}
