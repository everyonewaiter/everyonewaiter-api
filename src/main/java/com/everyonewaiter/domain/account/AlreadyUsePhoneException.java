package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyUsePhoneException extends BusinessException {

  public AlreadyUsePhoneException() {
    super(ErrorCode.ALREADY_USE_PHONE_NUMBER);
  }

}
