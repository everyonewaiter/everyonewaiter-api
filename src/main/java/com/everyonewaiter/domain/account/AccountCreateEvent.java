package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.Email;

public record AccountCreateEvent(Email email) {

  public AccountCreateEvent(Account account) {
    this(account.getEmail());
  }

}
