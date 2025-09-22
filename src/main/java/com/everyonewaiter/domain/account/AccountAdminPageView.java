package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.Email;
import java.time.Instant;

public record AccountAdminPageView(
    Long id,
    Email email,
    AccountState state,
    AccountPermission permission,
    boolean hasStore,
    Instant createdAt,
    Instant updatedAt
) {

}
