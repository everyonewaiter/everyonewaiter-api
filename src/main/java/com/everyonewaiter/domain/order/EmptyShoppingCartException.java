package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class EmptyShoppingCartException extends BusinessException {

  public EmptyShoppingCartException() {
    super(ErrorCode.NOT_EMPTY_ORDER_MENU);
  }

}
