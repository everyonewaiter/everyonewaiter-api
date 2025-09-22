package com.everyonewaiter.domain.staffcall;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class StaffCallOptionNotFoundException extends BusinessException {

  public StaffCallOptionNotFoundException() {
    super(ErrorCode.STAFF_CALL_OPTION_NOT_FOUND);
  }

}
