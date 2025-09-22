package com.everyonewaiter.application.device.provided;

import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DeviceCreateRequest;
import com.everyonewaiter.domain.device.DeviceUpdateRequest;
import jakarta.validation.Valid;

public interface DeviceManager {

  Device create(Long storeId, @Valid DeviceCreateRequest createRequest);

  Device update(Long deviceId, Long storeId, @Valid DeviceUpdateRequest updateRequest);

  void delete(Long deviceId, Long storeId);

}
