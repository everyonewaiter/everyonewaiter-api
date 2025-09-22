package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.PhoneNumber;
import java.time.Duration;

public record AuthSuccess(
    AuthPurpose purpose,
    PhoneNumber phoneNumber,
    Duration expiration
) implements Auth {

  private static final String KEY_PREFIX = "auth:success:";

  public AuthSuccess(AuthPurpose purpose, PhoneNumber phoneNumber) {
    this(purpose, phoneNumber, purpose.getExpiration());
  }

  @Override
  public String key() {
    return KEY_PREFIX + phoneNumber.value();
  }

  @Override
  public int value() {
    return -2;
  }

}
