package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExpiredVerificationPhoneException extends BusinessException {

  public ExpiredVerificationPhoneException() {
    super(ErrorCode.EXPIRED_VERIFICATION_PHONE_NUMBER);
  }

}
