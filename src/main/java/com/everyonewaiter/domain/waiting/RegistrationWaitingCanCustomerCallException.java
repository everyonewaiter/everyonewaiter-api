package com.everyonewaiter.domain.waiting;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class RegistrationWaitingCanCustomerCallException extends BusinessException {

  public RegistrationWaitingCanCustomerCallException() {
    super(ErrorCode.ONLY_REGISTRATION_STATE_CAN_BE_CALL);
  }

}
