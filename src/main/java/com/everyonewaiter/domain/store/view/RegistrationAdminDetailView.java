package com.everyonewaiter.domain.store.view;

import com.everyonewaiter.domain.store.entity.Registration;
import java.time.Instant;

public record RegistrationAdminDetailView(
    Long id,
    Long accountId,
    String email,
    String name,
    String ceoName,
    String address,
    String landline,
    String license,
    String image,
    Registration.Status status,
    Instant createdAt,
    Instant updatedAt
) {

}
