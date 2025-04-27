package com.everyonewaiter.domain.auth.entity;

import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.time.Duration;
import java.util.Random;

public record AuthCode(String phoneNumber, int code, Duration expiration) implements Auth {

  private static final int MIN = 100_000;
  private static final int MAX = 999_999;
  private static final Random RANDOM = new Random();
  private static final String KEY_PREFIX = "auth:code:";

  public AuthCode(String phoneNumber) {
    this(phoneNumber, RANDOM.nextInt(MAX - MIN + 1) + MIN, Duration.ofMinutes(5));
  }

  public AuthCode(String phoneNumber, int code) {
    this(phoneNumber, code, Duration.ofMinutes(5));
  }

  public void verify(int code) {
    if (code <= 0) {
      throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_CODE);
    }

    if (this.code != code) {
      throw new BusinessException(ErrorCode.UNMATCHED_VERIFICATION_CODE);
    }
  }

  @Override
  public String getKey() {
    return KEY_PREFIX + phoneNumber;
  }

  @Override
  public int getValue() {
    return code;
  }

}
