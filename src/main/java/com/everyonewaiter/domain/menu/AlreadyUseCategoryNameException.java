package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyUseCategoryNameException extends BusinessException {

  public AlreadyUseCategoryNameException() {
    super(ErrorCode.ALREADY_USE_CATEGORY_NAME);
  }

}
