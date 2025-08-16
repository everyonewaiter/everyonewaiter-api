package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.store.RegistrationAdminPageView;
import com.everyonewaiter.domain.store.RegistrationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "RegistrationAdminPageResponse")
public record RegistrationAdminPageResponse(
    @Schema(description = "매장 등록 ID", example = "\"694865267482835533\"")
    String registrationId,

    @Schema(description = "계정 ID", example = "\"694865267482835533\"")
    String accountId,

    @Schema(description = "이메일", example = "admin@everyonewaiter.com")
    String email,

    @Schema(description = "매장 이름", example = "나루 레스토랑")
    String name,

    @Schema(description = "매장 등록 신청 상태", example = "APPLY")
    RegistrationStatus status,

    @Schema(description = "매장 등록 신청일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "매장 등록 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static RegistrationAdminPageResponse from(RegistrationAdminPageView view) {
    return new RegistrationAdminPageResponse(
        view.id().toString(),
        view.accountId().toString(),
        view.email().address(),
        view.name(),
        view.status(),
        view.createdAt(),
        view.updatedAt()
    );
  }

}
