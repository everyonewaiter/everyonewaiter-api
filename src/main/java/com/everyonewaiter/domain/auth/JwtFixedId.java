package com.everyonewaiter.domain.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtFixedId {

  VERIFICATION_EMAIL(1000L),
  ;

  private final Long id;

  public boolean equalsId(Long id) {
    return this.id.equals(id);
  }

}
