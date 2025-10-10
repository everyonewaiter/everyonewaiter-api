package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class DuplicateMenuOptionNameException extends BusinessException {

  public DuplicateMenuOptionNameException() {
    super(ErrorCode.DUPLICATE_MENU_OPTION_NAME);
  }

}
