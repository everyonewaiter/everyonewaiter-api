package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class RejectedRegistrationCanReapplyException extends BusinessException {

  public RejectedRegistrationCanReapplyException() {
    super(ErrorCode.ONLY_REJECTED_REGISTRATION_CAN_BE_REAPPLY);
  }

}
