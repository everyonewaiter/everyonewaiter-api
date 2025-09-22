package com.everyonewaiter.domain.image;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class UnsupportedFileTypeException extends BusinessException {

  public UnsupportedFileTypeException() {
    super(ErrorCode.ALLOW_IMAGE_AND_PDF_FILE);
  }

}
