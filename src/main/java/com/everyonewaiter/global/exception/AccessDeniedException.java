package com.everyonewaiter.global.exception;

public class AccessDeniedException extends BusinessException {

  public AccessDeniedException() {
    super(ErrorCode.FORBIDDEN);
  }

}
