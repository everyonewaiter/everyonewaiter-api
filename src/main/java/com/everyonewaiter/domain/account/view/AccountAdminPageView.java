package com.everyonewaiter.domain.account.view;

import com.everyonewaiter.domain.account.entity.Account;
import java.time.Instant;

public record AccountAdminPageView(
    Long id,
    String email,
    Account.State state,
    Account.Permission permission,
    boolean hasStore,
    Instant createdAt,
    Instant updatedAt
) {

}
