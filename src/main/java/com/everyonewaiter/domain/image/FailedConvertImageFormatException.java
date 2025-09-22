package com.everyonewaiter.domain.image;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class FailedConvertImageFormatException extends BusinessException {

  public FailedConvertImageFormatException() {
    super(ErrorCode.FAILED_CONVERT_IMAGE_FORMAT);
  }

}
