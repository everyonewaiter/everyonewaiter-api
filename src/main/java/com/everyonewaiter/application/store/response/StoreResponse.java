package com.everyonewaiter.application.store.response;

import com.everyonewaiter.domain.store.entity.CountryOfOrigin;
import com.everyonewaiter.domain.store.entity.Setting;
import com.everyonewaiter.domain.store.entity.StaffCallOption;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.view.StoreView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreResponse {

  @Schema(name = "StoreResponse.Simples")
  public record Simples(List<Simple> stores) {

    public static Simples from(List<StoreView.Simple> views) {
      return new Simples(
          views.stream()
              .map(Simple::from)
              .toList()
      );
    }

  }

  @Schema(name = "StoreResponse.Simple")
  public record Simple(
      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "매장 이름", example = "홍길동식당")
      String name
  ) {

    public static Simple from(StoreView.Simple view) {
      return new Simple(view.id().toString(), view.name());
    }

  }

  @Schema(name = "StoreResponse.Detail")
  public record Detail(
      @Schema(description = "매장 ID", example = "\"694865267482835533\"")
      String storeId,

      @Schema(description = "계정 ID", example = "\"694865267482835533\"")
      String accountId,

      @Schema(description = "매장 이름", example = "홍길동식당")
      String name,

      @Schema(description = "대표자명", example = "홍길동")
      String ceoName,

      @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123")
      String address,

      @Schema(description = "매장 전화번호", example = "02-123-4567")
      String landline,

      @Schema(description = "사업자 등록번호", example = "443-60-00875")
      String license,

      @Schema(description = "사업자 등록증 이미지명", example = "license/202504/0KA652ZFZ26DG.webp")
      String image,

      @Schema(description = "매장 영업 상태", example = "OPEN")
      Store.Status status,

      @Schema(description = "마지막 매장 영업일", example = "2025-01-01 12:00:00")
      Instant lastOpenedAt,

      @Schema(description = "마지막 매장 마감일", example = "2025-01-01 12:00:00")
      Instant lastClosedAt,

      @Schema(description = "매장 설정")
      Settings setting,

      @Schema(description = "매장 생성일", example = "2025-01-01 12:00:00")
      Instant createdAt,

      @Schema(description = "매장 수정일", example = "2025-01-01 12:00:00")
      Instant updatedAt
  ) {

    public static Detail from(Store store) {
      return new Detail(
          Objects.requireNonNull(store.getId()).toString(),
          store.getAccountId().toString(),
          store.getBusinessLicense().getName(),
          store.getBusinessLicense().getCeoName(),
          store.getBusinessLicense().getAddress(),
          store.getBusinessLicense().getLandline(),
          store.getBusinessLicense().getLicense(),
          store.getBusinessLicense().getLicenseImage(),
          store.getStatus(),
          store.getLastOpenedAt(),
          store.getLastClosedAt(),
          Settings.from(store.getSetting()),
          store.getCreatedAt(),
          store.getUpdatedAt()
      );
    }

  }

  @Schema(name = "StoreResponse.Settings")
  public record Settings(
      @Schema(description = "KSNET 단말기 번호", example = "DPTOTEST01")
      String ksnetDeviceNo,

      @Schema(description = "POS 여분 테이블 표시 수", example = "5")
      int extraTableCount,

      @Schema(description = "주방 프린터와 연결된 기기 위치", example = "POS")
      Setting.PrinterLocation printerLocation,

      @Schema(description = "손님 테이블 메뉴 팝업 보이기 여부", example = "true")
      boolean showMenuPopup,

      @Schema(description = "손님 테이블 총 주문 금액 표시 여부", example = "true")
      boolean showOrderTotalPrice,

      @Schema(description = "원산지 정보")
      List<CountryOfOrigins> countryOfOrigins,

      @Schema(description = "직원 호출 옵션", example = "[\"직원 호출\", \"포장\"]")
      List<String> staffCallOptions
  ) {

    public static Settings from(Setting setting) {
      return new Settings(
          setting.getKsnetDeviceNo(),
          setting.getExtraTableCount(),
          setting.getPrinterLocation(),
          setting.isShowMenuPopup(),
          setting.isShowOrderTotalPrice(),
          setting.getCountryOfOrigins().stream().map(CountryOfOrigins::from).toList(),
          setting.getStaffCallOptions().stream().map(StaffCallOption::optionName).toList()
      );
    }

  }

  @Schema(name = "StoreResponse.CountryOfOrigins")
  public record CountryOfOrigins(
      @Schema(description = "품목", example = "돼지고기")
      String item,

      @Schema(description = "원산지", example = "국내산")
      String origin
  ) {

    public static CountryOfOrigins from(CountryOfOrigin countryOfOrigin) {
      return new CountryOfOrigins(countryOfOrigin.item(), countryOfOrigin.origin());
    }

  }

}
