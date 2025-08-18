package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class MenuNotFoundException extends BusinessException {

  public MenuNotFoundException() {
    super(ErrorCode.MENU_NOT_FOUND);
  }

}
