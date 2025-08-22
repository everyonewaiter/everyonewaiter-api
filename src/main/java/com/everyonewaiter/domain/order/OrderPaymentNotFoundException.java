package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class OrderPaymentNotFoundException extends BusinessException {

  public OrderPaymentNotFoundException() {
    super(ErrorCode.ORDER_PAYMENT_NOT_FOUND);
  }

}
