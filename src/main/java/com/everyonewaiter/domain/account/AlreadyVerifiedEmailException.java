package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyVerifiedEmailException extends BusinessException {

  public AlreadyVerifiedEmailException() {
    super(ErrorCode.ALREADY_VERIFIED_EMAIL);
  }

}
