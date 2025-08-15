package com.everyonewaiter.domain.image;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class FailedConvertPdfToImageException extends BusinessException {

  public FailedConvertPdfToImageException() {
    super(ErrorCode.FAILED_CONVERT_PDF_TO_IMAGE);
  }

}
