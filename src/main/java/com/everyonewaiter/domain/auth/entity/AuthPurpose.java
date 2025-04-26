package com.everyonewaiter.domain.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthPurpose {
  SIGN_UP(5),
  CREATE_DEVICE(50),
  ;

  private final int maxAttempt;

}
