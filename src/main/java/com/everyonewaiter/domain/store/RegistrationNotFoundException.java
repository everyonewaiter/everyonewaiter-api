package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class RegistrationNotFoundException extends BusinessException {

  public RegistrationNotFoundException() {
    super(ErrorCode.STORE_REGISTRATION_NOT_FOUND);
  }

}
