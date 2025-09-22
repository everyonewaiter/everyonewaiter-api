package com.everyonewaiter.domain.account;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AccountNotFoundException extends BusinessException {

  public AccountNotFoundException() {
    super(ErrorCode.ACCOUNT_NOT_FOUND);
  }

}
