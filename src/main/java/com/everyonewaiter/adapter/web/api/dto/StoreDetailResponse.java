package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "StoreDetailResponse")
public record StoreDetailResponse(
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
    StoreStatus status,

    @Schema(description = "마지막 매장 영업일", example = "2025-01-01 12:00:00")
    Instant lastOpenedAt,

    @Schema(description = "마지막 매장 마감일", example = "2025-01-01 12:00:00")
    Instant lastClosedAt,

    @Schema(description = "매장 설정")
    StoreSettingDetailResponse setting,

    @Schema(description = "매장 생성일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "매장 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static StoreDetailResponse from(Store store) {
    return new StoreDetailResponse(
        String.valueOf(store.getId()),
        String.valueOf(store.getAccount().getId()),
        store.getDetail().getName(),
        store.getDetail().getCeoName(),
        store.getDetail().getAddress(),
        store.getDetail().getLandline(),
        store.getDetail().getLicense().value(),
        store.getDetail().getLicenseImage(),
        store.getStatus(),
        store.getLastOpenedAt(),
        store.getLastClosedAt(),
        StoreSettingDetailResponse.from(store.getSetting()),
        store.getCreatedAt(),
        store.getUpdatedAt()
    );
  }

}
