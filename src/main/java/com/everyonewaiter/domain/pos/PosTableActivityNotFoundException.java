package com.everyonewaiter.domain.pos;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class PosTableActivityNotFoundException extends BusinessException {

  public PosTableActivityNotFoundException() {
    super(ErrorCode.POS_TABLE_ACTIVITY_NOT_FOUND);
  }

}
