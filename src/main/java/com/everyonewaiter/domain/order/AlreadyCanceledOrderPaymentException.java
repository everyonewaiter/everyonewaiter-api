package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyCanceledOrderPaymentException extends BusinessException {

  public AlreadyCanceledOrderPaymentException() {
    super(ErrorCode.ALREADY_CANCELED_ORDER_PAYMENT);
  }

}
