package com.everyonewaiter.domain.auth;

public record JwtPayload(Long id, String subject) {

  public JwtPayload(JwtFixedId fixedId, String subject) {
    this(fixedId.getId(), subject);
  }

}
