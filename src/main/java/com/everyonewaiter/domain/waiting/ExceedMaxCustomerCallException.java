package com.everyonewaiter.domain.waiting;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExceedMaxCustomerCallException extends BusinessException {

  public ExceedMaxCustomerCallException() {
    super(ErrorCode.EXCEED_MAXIMUM_CUSTOMER_CALL_COUNT);
  }

}
