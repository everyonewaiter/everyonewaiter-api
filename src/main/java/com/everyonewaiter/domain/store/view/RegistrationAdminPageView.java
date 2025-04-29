package com.everyonewaiter.domain.store.view;

import com.everyonewaiter.domain.store.entity.Registration;
import java.time.Instant;

public record RegistrationAdminPageView(
    Long id,
    Long accountId,
    String email,
    String name,
    Registration.Status status,
    Instant createdAt,
    Instant updatedAt
) {

}
