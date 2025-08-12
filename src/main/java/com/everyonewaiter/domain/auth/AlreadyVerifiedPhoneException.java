package com.everyonewaiter.domain.auth;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyVerifiedPhoneException extends BusinessException {

  public AlreadyVerifiedPhoneException() {
    super(ErrorCode.ALREADY_VERIFIED_PHONE_NUMBER);
  }

}
