package com.everyonewaiter.application.store.response;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.view.RegistrationAdminPageView;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationAdmin {

  @Schema(name = "RegistrationAdmin.PageViewResponse")
  public record PageViewResponse(
      @Schema(description = "매장 등록 ID", example = "\"694865267482835533\"")
      String registrationId,

      @Schema(description = "계정 ID", example = "\"694865267482835533\"")
      String accountId,

      @Schema(description = "이메일", example = "admin@everyonewaiter.com")
      String email,

      @Schema(description = "매장 이름", example = "나루 레스토랑")
      String name,

      @Schema(description = "매장 등록 신청 상태", example = "APPLY")
      Registration.Status status,

      @Schema(description = "매장 등록 신청일", example = "2025-01-01 12:00:00")
      Instant createdAt,

      @Schema(description = "매장 등록 수정일", example = "2025-01-01 12:00:00")
      Instant updatedAt
  ) {

    public static PageViewResponse from(RegistrationAdminPageView adminPageView) {
      return new PageViewResponse(
          adminPageView.id().toString(),
          adminPageView.accountId().toString(),
          adminPageView.email(),
          adminPageView.name(),
          adminPageView.status(),
          adminPageView.createdAt(),
          adminPageView.updatedAt()
      );
    }

  }

}
