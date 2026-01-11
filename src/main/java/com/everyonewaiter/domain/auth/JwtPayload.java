package com.everyonewaiter.domain.auth;

import static java.util.Objects.requireNonNull;

public record JwtPayload(String id, String subject) {

  public JwtPayload(Long id, String subject) {
    this(requireNonNull(id).toString(), subject);
  }

  public Long getLongId() throws NumberFormatException {
    return Long.parseLong(id);
  }

}
