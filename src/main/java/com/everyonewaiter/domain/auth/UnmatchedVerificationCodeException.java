package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class UnmatchedVerificationCodeException extends BusinessException {

  public UnmatchedVerificationCodeException() {
    super(ErrorCode.UNMATCHED_VERIFICATION_CODE);
  }

}
