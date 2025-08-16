package com.everyonewaiter.domain.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AppliedRegistrationCanRejectException extends BusinessException {

  public AppliedRegistrationCanRejectException() {
    super(ErrorCode.ONLY_APPLY_OR_REAPPLY_STATUS_CAN_BE_REJECT);
  }

}
