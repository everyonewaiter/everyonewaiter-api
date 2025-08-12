package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class DisabledAccountException extends BusinessException {

  public DisabledAccountException() {
    super(ErrorCode.DISABLED_ACCOUNT);
  }

}
