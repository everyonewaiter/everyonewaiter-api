package com.everyonewaiter.application.device.provided;

import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DeviceUpdateRequest;
import jakarta.validation.Valid;

public interface DeviceUpdater {

  Device update(Long deviceId, Long storeId, @Valid DeviceUpdateRequest updateRequest);

}
