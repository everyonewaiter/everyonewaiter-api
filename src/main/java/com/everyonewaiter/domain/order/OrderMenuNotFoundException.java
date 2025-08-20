package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class OrderMenuNotFoundException extends BusinessException {

  public OrderMenuNotFoundException() {
    super(ErrorCode.ORDER_MENU_NOT_FOUND);
  }

}
