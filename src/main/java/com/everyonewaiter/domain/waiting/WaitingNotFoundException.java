package com.everyonewaiter.domain.waiting;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class WaitingNotFoundException extends BusinessException {

  public WaitingNotFoundException() {
    super(ErrorCode.WAITING_NOT_FOUND);
  }

}
