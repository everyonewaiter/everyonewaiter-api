package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AppliedRegistrationCanApproveException extends BusinessException {

  public AppliedRegistrationCanApproveException() {
    super(ErrorCode.ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_APPROVE);
  }

}
