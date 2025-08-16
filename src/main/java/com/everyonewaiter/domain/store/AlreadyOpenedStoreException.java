package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyOpenedStoreException extends BusinessException {

  public AlreadyOpenedStoreException() {
    super(ErrorCode.ALREADY_STORE_OPENED);
  }

}
