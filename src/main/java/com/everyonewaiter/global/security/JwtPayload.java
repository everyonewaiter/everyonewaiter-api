package com.everyonewaiter.global.security;

public record JwtPayload(Long id, String subject) {

  public JwtPayload(JwtFixedId fixedId, String subject) {
    this(fixedId.getId(), subject);
  }

}
