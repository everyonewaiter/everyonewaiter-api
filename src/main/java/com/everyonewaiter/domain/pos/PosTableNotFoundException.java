package com.everyonewaiter.domain.pos;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class PosTableNotFoundException extends BusinessException {

  public PosTableNotFoundException() {
    super(ErrorCode.POS_TABLE_NOT_FOUND);
  }

}
