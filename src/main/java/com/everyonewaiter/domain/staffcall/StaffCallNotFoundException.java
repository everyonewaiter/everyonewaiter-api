package com.everyonewaiter.domain.staffcall;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class StaffCallNotFoundException extends BusinessException {

  public StaffCallNotFoundException() {
    super(ErrorCode.STAFF_CALL_NOT_FOUND);
  }

}
