package com.everyonewaiter.application.account.service.request;

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

    Pagination pagination
) {

  public AccountAdminPage(
      @Nullable String email,
      @Nullable Account.State state,
      @Nullable Account.Permission permission,
      long page,
      long size
  ) {
    this(email, state, permission, new Pagination(page, size));
  }

}
