package com.everyonewaiter.global.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtFixedId {
  VERIFICATION_EMAIL(1L),
  ;

  private final Long id;
}
