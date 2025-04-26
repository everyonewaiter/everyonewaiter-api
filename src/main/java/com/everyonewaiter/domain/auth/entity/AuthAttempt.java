package com.everyonewaiter.domain.auth.entity;

import java.time.Duration;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(exclude = "expiration")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthAttempt implements Auth {

  private static final String KEY_PREFIX = "auth:attempt:";

  private String phoneNumber;
  private AuthPurpose purpose;
  private Duration expiration;

  public static AuthAttempt create(String phoneNumber, AuthPurpose purpose) {
    AuthAttempt authAttempt = new AuthAttempt();
    authAttempt.phoneNumber = phoneNumber;
    authAttempt.purpose = purpose;
    authAttempt.expiration = Duration.ofDays(1);
    return authAttempt;
  }

  public boolean isExceed(int count) {
    return purpose.getMaxAttempt() <= count;
  }

  @Override
  public String getKey() {
    return KEY_PREFIX + purpose.name().toLowerCase() + ":" + phoneNumber;
  }

  @Override
  public int getValue() {
    return 0;
  }

}
