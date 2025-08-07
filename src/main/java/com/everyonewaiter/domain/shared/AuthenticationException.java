package com.everyonewaiter.domain.shared;

public class AuthenticationException extends BusinessException {

  public AuthenticationException() {
    super(ErrorCode.UNAUTHORIZED);
  }

  public AuthenticationException(String message) {
    super(ErrorCode.UNAUTHORIZED, message);
  }

}
