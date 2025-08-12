package com.everyonewaiter.domain.auth;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthPurpose {

  SIGN_UP(5, Duration.ofMinutes(15)),
  CREATE_DEVICE(50, Duration.ofMinutes(30)),
  ;

  private final int maxAttempt;
  private final Duration expiration;

  public boolean isExceed(int count) {
    return maxAttempt <= count;
  }

  public String getLowerCaseName() {
    return name().toLowerCase();
  }

}
