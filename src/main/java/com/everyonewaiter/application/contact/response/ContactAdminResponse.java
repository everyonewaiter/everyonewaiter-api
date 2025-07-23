package com.everyonewaiter.application.contact.response;

import com.everyonewaiter.domain.contact.entity.Contact;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactAdminResponse {

  @Schema(name = "AccountAdminResponse.PageView")
  public record PageView(
      @Schema(description = "문의 ID", example = "\"694865267482835533\"")
      String contactId,

      @Schema(description = "상호명", example = "홍길동식당")
      String name,

      @Schema(description = "휴대폰 번호", example = "01044591812")
      String phoneNumber,

      @Schema(description = "사업자 번호", example = "443-60-00875")
      String license,

      @Schema(description = "문의 활성 여부", examples = "true")
      boolean active,

      @Schema(description = "문의 생성일", example = "2025-01-01 12:00:00")
      Instant createdAt,

      @Schema(description = "문의 수정일", example = "2025-01-01 12:00:00")
      Instant updatedAt
  ) {

    public static PageView from(Contact contact) {
      return new PageView(
          Objects.requireNonNull(contact.getId()).toString(),
          contact.getName(),
          contact.getPhoneNumber(),
          contact.getLicense(),
          contact.isActive(),
          contact.getCreatedAt(),
          contact.getUpdatedAt()
      );
    }

  }

}
