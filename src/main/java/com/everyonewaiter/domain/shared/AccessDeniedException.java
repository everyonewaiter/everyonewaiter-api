package com.everyonewaiter.domain.shared;

public class AccessDeniedException extends BusinessException {

  public AccessDeniedException() {
    super(ErrorCode.FORBIDDEN);
  }

  public AccessDeniedException(String message) {
    super(ErrorCode.FORBIDDEN, message);
  }

}
