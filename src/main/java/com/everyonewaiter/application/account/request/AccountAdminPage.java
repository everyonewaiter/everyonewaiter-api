package com.everyonewaiter.application.account.request;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.support.Pagination;
import jakarta.annotation.Nullable;

public record AccountAdminPage(
    @Nullable
    String email,

    @Nullable
    Account.State state,

    @Nullable
    Account.Permission permission,

    @Nullable
    Boolean hasStore,

    Pagination pagination
) {

  public AccountAdminPage(
      @Nullable String email,
      @Nullable Account.State state,
      @Nullable Account.Permission permission,
      @Nullable Boolean hasStore,
      long page,
      long size
  ) {
    this(email, state, permission, hasStore, new Pagination(page, size));
  }

}
