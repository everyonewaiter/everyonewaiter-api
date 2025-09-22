package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyCanceledOrderException extends BusinessException {

  public AlreadyCanceledOrderException() {
    super(ErrorCode.ALREADY_CANCELED_ORDER);
  }

}
