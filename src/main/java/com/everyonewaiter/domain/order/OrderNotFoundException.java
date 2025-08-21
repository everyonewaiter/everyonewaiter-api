package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class OrderNotFoundException extends BusinessException {

  public OrderNotFoundException() {
    super(ErrorCode.ORDER_NOT_FOUND);
  }

}
