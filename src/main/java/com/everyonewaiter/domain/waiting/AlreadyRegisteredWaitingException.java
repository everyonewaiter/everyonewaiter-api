package com.everyonewaiter.domain.waiting;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyRegisteredWaitingException extends BusinessException {

  public AlreadyRegisteredWaitingException() {
    super(ErrorCode.ALREADY_REGISTERED_WAITING);
  }

}
