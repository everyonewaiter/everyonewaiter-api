package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class MenuOptionNotFoundException extends BusinessException {

  public MenuOptionNotFoundException() {
    super(ErrorCode.MENU_OPTION_NOT_FOUND);
  }

}
