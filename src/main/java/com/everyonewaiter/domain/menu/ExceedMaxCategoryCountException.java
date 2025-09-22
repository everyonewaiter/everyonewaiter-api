package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExceedMaxCategoryCountException extends BusinessException {

  public ExceedMaxCategoryCountException() {
    super(ErrorCode.EXCEED_MAXIMUM_CATEGORY_COUNT);
  }

}
