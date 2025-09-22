package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "RegistrationDetailResponse")
public record RegistrationDetailResponse(
    @Schema(description = "매장 등록 ID", example = "\"694865267482835533\"")
    String registrationId,

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

    @Schema(description = "매장 등록 신청 상태", example = "APPLY")
    RegistrationStatus status,

    @Schema(description = "매장 등록 거부 사유", example = "사업자 정보를 조회할 수 없습니다.")
    String reason,

    @Schema(description = "매장 등록 신청일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "매장 등록 신청 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static RegistrationDetailResponse from(Registration registration) {
    return new RegistrationDetailResponse(
        String.valueOf(registration.getId()),
        String.valueOf(registration.getAccount().getId()),
        registration.getDetail().getName(),
        registration.getDetail().getCeoName(),
        registration.getDetail().getAddress(),
        registration.getDetail().getLandline(),
        registration.getDetail().getLicense().value(),
        registration.getDetail().getLicenseImage(),
        registration.getStatus(),
        registration.getRejectReason(),
        registration.getCreatedAt(),
        registration.getUpdatedAt()
    );
  }

}
