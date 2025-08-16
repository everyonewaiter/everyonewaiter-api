package com.everyonewaiter.domain.device;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;

public class DeviceNotFoundException extends BusinessException {

  public DeviceNotFoundException() {
    super(ErrorCode.DEVICE_NOT_FOUND);
  }

}
