package com.everyonewaiter.application.account.dto;

import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import java.time.Instant;

public record AccountAdminPageView(
    Long id,
    String email,
    AccountState state,
    AccountPermission permission,
    boolean hasStore,
    Instant createdAt,
    Instant updatedAt
) {

}
