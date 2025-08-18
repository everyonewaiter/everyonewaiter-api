package com.everyonewaiter.domain.waiting;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class RegistrationWaitingCanCancelException extends BusinessException {

  public RegistrationWaitingCanCancelException() {
    super(ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_CANCEL);
  }

}
