package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.PhoneNumber;
import java.time.Duration;
import java.util.Random;

public record AuthCode(PhoneNumber phoneNumber, int code, Duration expiration) implements Auth {

  private static final int MIN = 100_000;
  private static final int MAX = 999_999;
  private static final Random RANDOM = new Random();
  private static final String KEY_PREFIX = "auth:code:";

  public AuthCode(PhoneNumber phoneNumber) {
    this(phoneNumber, RANDOM.nextInt(MAX - MIN + 1) + MIN, Duration.ofMinutes(5));
  }

  public AuthCode(PhoneNumber phoneNumber, int code) {
    this(phoneNumber, code, Duration.ofMinutes(5));
  }

  public void verify(int code) {
    if (code <= 0) {
      throw new ExpiredVerificationCodeException();
    }

    if (this.code != code) {
      throw new UnmatchedVerificationCodeException();
    }
  }

  @Override
  public String key() {
    return KEY_PREFIX + phoneNumber.value();
  }

  @Override
  public int value() {
    return code;
  }

}
