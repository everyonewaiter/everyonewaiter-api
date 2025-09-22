package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ClosedStoreException extends BusinessException {

  public ClosedStoreException() {
    super(ErrorCode.STORE_IS_CLOSED);
  }

}
