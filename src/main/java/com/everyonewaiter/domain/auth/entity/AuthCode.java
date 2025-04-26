package com.everyonewaiter.domain.auth.entity;

import java.time.Duration;
import java.util.Random;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(exclude = "expiration")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthCode implements Auth {

  private static final int MIN = 100_000;
  private static final int MAX = 999_999;
  private static final Random RANDOM = new Random();
  private static final String KEY_PREFIX = "auth:code:";

  private String phoneNumber;
  private int code;
  private Duration expiration;

  public static AuthCode create(String phoneNumber) {
    AuthCode authCode = new AuthCode();
    authCode.phoneNumber = phoneNumber;
    authCode.code = RANDOM.nextInt(MAX - MIN + 1) + MIN;
    authCode.expiration = Duration.ofMinutes(5);
    return authCode;
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
