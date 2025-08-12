package com.everyonewaiter.domain.auth;

import static java.util.Objects.requireNonNull;

public record JwtPayload(Long id, String subject) {

  public JwtPayload(Long id, Long subject) {
    this(id, requireNonNull(subject).toString());
  }

  public Long parseLongSubject() throws NumberFormatException {
    return Long.parseLong(subject());
  }

}
