package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExpiredVerificationCodeException extends BusinessException {

  public ExpiredVerificationCodeException() {
    super(ErrorCode.EXPIRED_VERIFICATION_CODE);
  }

}
