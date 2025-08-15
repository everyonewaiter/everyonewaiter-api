package com.everyonewaiter.domain.image;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class FailedUploadImageException extends BusinessException {

  public FailedUploadImageException() {
    super(ErrorCode.FAILED_UPLOAD_IMAGE);
  }

}
