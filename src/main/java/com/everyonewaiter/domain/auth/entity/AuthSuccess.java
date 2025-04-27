package com.everyonewaiter.domain.auth.entity;

import java.time.Duration;

public record AuthSuccess(
    String phoneNumber,
    AuthPurpose purpose,
    Duration expiration
) implements Auth {

  private static final String KEY_PREFIX = "auth:success:";

  public AuthSuccess(String phoneNumber, AuthPurpose purpose) {
    this(phoneNumber, purpose, purpose.getExpiration());
  }

  @Override
  public String getKey() {
    return KEY_PREFIX + phoneNumber;
  }

  @Override
  public int getValue() {
    return -2;
  }

}
