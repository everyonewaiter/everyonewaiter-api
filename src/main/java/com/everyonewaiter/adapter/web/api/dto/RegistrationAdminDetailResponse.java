package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "RegistrationAdminDetailResponse")
public record RegistrationAdminDetailResponse(
    @Schema(description = "매장 등록 ID", example = "\"694865267482835533\"")
    String registrationId,

    @Schema(description = "계정 ID", example = "\"694865267482835533\"")
    String accountId,

    @Schema(description = "이메일", example = "admin@everyonewaiter.com")
    String email,

    @Schema(description = "매장 이름", example = "나루 레스토랑")
    String name,

    @Schema(description = "대표자명", example = "홍길동")
    String ceoName,

    @Schema(description = "매장 주소", example = "경상남도 창원시 의창구 123")
    String address,

    @Schema(description = "매장 전화번호", example = "홍길동")
    String landline,

    @Schema(description = "사업자 등록번호", example = "홍길동")
    String license,

    @Schema(description = "사업자 등록증 이미지명", example = "license/202504/0KA652ZFZ26DG.webp")
    String image,

    @Schema(description = "매장 등록 신청 상태", example = "APPLY")
    RegistrationStatus status,

    @Schema(description = "매장 등록 신청일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "매장 등록 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static RegistrationAdminDetailResponse from(Registration registration) {
    return new RegistrationAdminDetailResponse(
        String.valueOf(registration.getId()),
        String.valueOf(registration.getAccount().getId()),
        registration.getAccount().getEmail().address(),
        registration.getDetail().getName(),
        registration.getDetail().getCeoName(),
        registration.getDetail().getAddress(),
        registration.getDetail().getLandline(),
        registration.getDetail().getLicense().value(),
        registration.getDetail().getLicenseImage(),
        registration.getStatus(),
        registration.getCreatedAt(),
        registration.getUpdatedAt()
    );
  }

}
