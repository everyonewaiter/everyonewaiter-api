package com.everyonewaiter.domain.device;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class AlreadyUseDeviceNameException extends BusinessException {

  public AlreadyUseDeviceNameException() {
    super(ErrorCode.ALREADY_USE_DEVICE_NAME);
  }

}
