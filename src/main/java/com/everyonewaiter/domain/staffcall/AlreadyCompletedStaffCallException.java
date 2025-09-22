package com.everyonewaiter.domain.staffcall;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyCompletedStaffCallException extends BusinessException {

  public AlreadyCompletedStaffCallException() {
    super(ErrorCode.ALREADY_COMPLETED_STAFF_CALL);
  }

}
