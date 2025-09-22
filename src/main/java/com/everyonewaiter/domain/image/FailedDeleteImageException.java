package com.everyonewaiter.domain.image;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class FailedDeleteImageException extends BusinessException {

  public FailedDeleteImageException() {
    super(ErrorCode.FAILED_DELETE_IMAGE);
  }

}
