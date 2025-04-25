package com.everyonewaiter.global.exception;

public class AuthenticationException extends BusinessException {

  public AuthenticationException() {
    super(ErrorCode.UNAUTHORIZED);
  }

}
