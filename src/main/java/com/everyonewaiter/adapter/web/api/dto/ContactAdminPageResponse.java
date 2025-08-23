package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.contact.Contact;
import com.everyonewaiter.domain.contact.ContactState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "ContactAdminPageResponse")
public record ContactAdminPageResponse(
    @Schema(description = "서비스 도입 문의 ID", example = "\"694865267482835533\"")
    String contactId,

    @Schema(description = "상호명", example = "홍길동식당")
    String storeName,

    @Schema(description = "휴대폰 번호", example = "01044591812")
    String phoneNumber,

    @Schema(description = "사업자 번호", example = "443-60-00875")
    String license,

    @Schema(description = "서비스 도입 문의 진행 상태", examples = "PENDING")
    ContactState state,

    @Schema(description = "서비스 도입 문의 생성일", example = "2025-01-01 12:00:00")
    Instant createdAt,

    @Schema(description = "서비스 도입 문의 수정일", example = "2025-01-01 12:00:00")
    Instant updatedAt
) {

  public static ContactAdminPageResponse from(Contact contact) {
    return new ContactAdminPageResponse(
        String.valueOf(contact.getId()),
        contact.getStoreName(),
        contact.getPhoneNumber().value(),
        contact.getLicense().value(),
        contact.getState(),
        contact.getCreatedAt(),
        contact.getUpdatedAt()
    );
  }

}
