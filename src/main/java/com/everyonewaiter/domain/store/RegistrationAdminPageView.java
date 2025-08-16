package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.Email;
import java.time.Instant;

public record RegistrationAdminPageView(
    Long id,
    Long accountId,
    Email email,
    String name,
    RegistrationStatus status,
    Instant createdAt,
    Instant updatedAt
) {

}
