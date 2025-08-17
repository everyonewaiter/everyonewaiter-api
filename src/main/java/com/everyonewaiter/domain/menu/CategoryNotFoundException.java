package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class CategoryNotFoundException extends BusinessException {

  public CategoryNotFoundException() {
    super(ErrorCode.CATEGORY_NOT_FOUND);
  }

}
