package com.everyonewaiter.domain.health;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class ApkVersionNotFoundException extends BusinessException {

  public ApkVersionNotFoundException() {
    super(ErrorCode.APK_VERSION_NOT_FOUND);
  }

}
