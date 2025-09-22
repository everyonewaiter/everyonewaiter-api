package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class InvalidOrderMenuQuantityException extends BusinessException {

  public InvalidOrderMenuQuantityException() {
    super(ErrorCode.ORDER_MENU_QUANTITY_POSITIVE);
  }

}
