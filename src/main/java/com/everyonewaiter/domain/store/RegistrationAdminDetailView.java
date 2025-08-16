package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessLicense;
import com.everyonewaiter.domain.shared.Email;
import java.time.Instant;

public record RegistrationAdminDetailView(
    Long id,
    Long accountId,
    Email email,
    String name,
    String ceoName,
    String address,
    String landline,
    BusinessLicense license,
    String image,
    RegistrationStatus status,
    Instant createdAt,
    Instant updatedAt
) {

}
