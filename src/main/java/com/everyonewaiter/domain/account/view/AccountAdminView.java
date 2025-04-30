package com.everyonewaiter.domain.account.view;

import com.everyonewaiter.domain.account.entity.Account;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAdminView {

  public record Page(
      Long id,
      String email,
      Account.State state,
      Account.Permission permission,
      boolean hasStore,
      Instant createdAt,
      Instant updatedAt
  ) {

  }

}
