package com.everyonewaiter.application.account.request;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.shared.Pagination;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAdminRead {

  public record PageView(
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

    public PageView(
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

}
