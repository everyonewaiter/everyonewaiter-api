package com.everyonewaiter.domain.order;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class IncludeSoldOutMenuException extends BusinessException {

  public IncludeSoldOutMenuException() {
    super(ErrorCode.INCLUDE_SOLD_OUT_MENU);
  }

}
