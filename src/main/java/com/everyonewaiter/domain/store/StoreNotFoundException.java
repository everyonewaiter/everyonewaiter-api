package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class StoreNotFoundException extends BusinessException {

  public StoreNotFoundException() {
    super(ErrorCode.STORE_NOT_FOUND);
  }

}
