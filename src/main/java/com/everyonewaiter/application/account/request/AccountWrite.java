package com.everyonewaiter.application.account.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountWrite {

  public record Create(String email, String password, String phoneNumber) {

  }

  public record SignIn(String email, String password) {

  }

}
