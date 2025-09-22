package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class FailedSignInException extends BusinessException {

  public FailedSignInException() {
    super(ErrorCode.FAILED_SIGN_IN);
  }

}
