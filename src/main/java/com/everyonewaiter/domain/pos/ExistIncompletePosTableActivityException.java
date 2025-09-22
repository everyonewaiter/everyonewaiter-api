package com.everyonewaiter.domain.pos;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ExistIncompletePosTableActivityException extends BusinessException {

  public ExistIncompletePosTableActivityException() {
    super(ErrorCode.INCOMPLETE_POS_TABLE_ACTIVITY);
  }

}
