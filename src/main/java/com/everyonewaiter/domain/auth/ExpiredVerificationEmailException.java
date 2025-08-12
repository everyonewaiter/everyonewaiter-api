package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExpiredVerificationEmailException extends BusinessException {

  public ExpiredVerificationEmailException() {
    super(ErrorCode.EXPIRED_VERIFICATION_EMAIL);
  }

}
