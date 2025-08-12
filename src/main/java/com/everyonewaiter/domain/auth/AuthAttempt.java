package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.PhoneNumber;
import java.time.Duration;

public record AuthAttempt(
    AuthPurpose purpose,
    PhoneNumber phoneNumber,
    Duration expiration
) implements Auth {

  private static final String KEY_PREFIX = "auth:attempt:";

  public AuthAttempt(AuthPurpose purpose, PhoneNumber phoneNumber) {
    this(purpose, phoneNumber, Duration.ofDays(1));
  }

  public boolean isExceed(int count) {
    return purpose.isExceed(count);
  }

  @Override
  public String key() {
    return KEY_PREFIX + purpose.getLowerCaseName() + ":" + phoneNumber.value();
  }

  @Override
  public int value() {
    return -2;
  }

}
