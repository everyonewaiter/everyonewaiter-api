package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class NotCompleteEmailVerificationException extends BusinessException {

  public NotCompleteEmailVerificationException() {
    super(ErrorCode.NOT_COMPLETE_EMAIL_VERIFICATION);
  }

}
