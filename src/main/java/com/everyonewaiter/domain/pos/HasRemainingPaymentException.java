package com.everyonewaiter.domain.pos;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class HasRemainingPaymentException extends BusinessException {

  public HasRemainingPaymentException() {
    super(ErrorCode.HAS_REMAINING_PAYMENT_PRICE);
  }

}
