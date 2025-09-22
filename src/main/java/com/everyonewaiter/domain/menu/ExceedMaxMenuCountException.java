package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExceedMaxMenuCountException extends BusinessException {

  public ExceedMaxMenuCountException() {
    super(ErrorCode.EXCEED_MAXIMUM_MENU_COUNT);
  }

}
