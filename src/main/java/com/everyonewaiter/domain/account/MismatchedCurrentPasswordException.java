package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class MismatchedCurrentPasswordException extends BusinessException {

  public MismatchedCurrentPasswordException() {
    super(ErrorCode.MISMATCHED_CURRENT_PASSWORD);
  }

}
