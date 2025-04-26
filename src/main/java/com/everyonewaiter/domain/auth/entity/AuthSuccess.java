package com.everyonewaiter.domain.auth.entity;

import java.time.Duration;

public record AuthSuccess(String phoneNumber, Duration expiration) implements Auth {

  private static final String KEY_PREFIX = "auth:success:";

  public AuthSuccess(String phoneNumber) {
    this(phoneNumber, Duration.ofMinutes(15));
  }

  @Override
  public String getKey() {
    return KEY_PREFIX + phoneNumber;
  }

  @Override
  public int getValue() {
    return -2;
  }

  @Override
  public Duration getExpiration() {
    return expiration;
  }

}
