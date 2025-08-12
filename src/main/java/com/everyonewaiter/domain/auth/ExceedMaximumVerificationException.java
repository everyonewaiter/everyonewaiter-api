package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExceedMaximumVerificationException extends BusinessException {

  public ExceedMaximumVerificationException() {
    super(ErrorCode.EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER);
  }

}
