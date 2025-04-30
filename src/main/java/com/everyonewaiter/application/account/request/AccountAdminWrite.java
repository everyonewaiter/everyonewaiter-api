package com.everyonewaiter.application.account.request;

import com.everyonewaiter.domain.account.entity.Account;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAdminWrite {

  public record Update(Account.State state, Account.Permission permission) {

  }

}
