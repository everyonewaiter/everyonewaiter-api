package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyUseEmailException extends BusinessException {

  public AlreadyUseEmailException() {
    super(ErrorCode.ALREADY_USE_EMAIL);
  }

}
