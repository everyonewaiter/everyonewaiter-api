package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyCompletedServingOrderException extends BusinessException {

  public AlreadyCompletedServingOrderException() {
    super(ErrorCode.ALREADY_COMPLETED_SERVING);
  }

}
