package com.everyonewaiter.application.device;

import com.everyonewaiter.application.device.provided.DeviceValidator;
import com.everyonewaiter.application.device.required.DeviceRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.device.AlreadyUseDeviceNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class DeviceValidateService implements DeviceValidator {

  private final DeviceRepository deviceRepository;

  @Override
  public void checkDuplicateName(Long storeId, String name) {
    if (deviceRepository.exists(storeId, name)) {
      throw new AlreadyUseDeviceNameException();
    }
  }

  @Override
  public void checkDuplicateNameExcludeId(Long deviceId, Long storeId, String name) {
    if (deviceRepository.existsExcludeId(deviceId, storeId, name)) {
      throw new AlreadyUseDeviceNameException();
    }
  }

}
