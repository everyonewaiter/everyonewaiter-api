package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyClosedStoreException extends BusinessException {

  public AlreadyClosedStoreException() {
    super(ErrorCode.ALREADY_STORE_CLOSED);
  }

}
