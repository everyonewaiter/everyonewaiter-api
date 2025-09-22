package com.everyonewaiter.domain.pos;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class PosTableActiveActivityNotFoundException extends BusinessException {

  public PosTableActiveActivityNotFoundException() {
    super(ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND);
  }

}
