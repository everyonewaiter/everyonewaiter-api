package com.everyonewaiter.domain.waiting;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExistIncompleteWaitingException extends BusinessException {

  public ExistIncompleteWaitingException() {
    super(ErrorCode.INCOMPLETE_WAITING);
  }

}
