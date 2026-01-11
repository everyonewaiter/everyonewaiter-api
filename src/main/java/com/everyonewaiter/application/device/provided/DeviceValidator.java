package com.everyonewaiter.application.device.provided;

public interface DeviceValidator {

  void checkDuplicateName(Long storeId, String name);

  void checkDuplicateNameExcludeId(Long deviceId, Long storeId, String name);

}
