package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class InvalidMenuOptionDiscountPriceException extends BusinessException {

  public InvalidMenuOptionDiscountPriceException() {
    super(ErrorCode.INVALID_DISCOUNT_OPTION_PRICE);
  }

}
