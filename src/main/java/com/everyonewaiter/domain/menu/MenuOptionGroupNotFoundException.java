package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class MenuOptionGroupNotFoundException extends BusinessException {

  public MenuOptionGroupNotFoundException() {
    super(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND);
  }

}
