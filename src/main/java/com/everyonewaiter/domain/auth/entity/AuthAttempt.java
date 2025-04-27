package com.everyonewaiter.domain.auth.entity;

import java.time.Duration;

public record AuthAttempt(
    String phoneNumber,
    AuthPurpose purpose,
    Duration expiration
) implements Auth {

  private static final String KEY_PREFIX = "auth:attempt:";

  public AuthAttempt(String phoneNumber, AuthPurpose purpose) {
    this(phoneNumber, purpose, Duration.ofDays(1));
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
    return -2;
  }

}
