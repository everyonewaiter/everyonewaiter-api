package com.everyonewaiter.application.device.provided;

import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DeviceCreateRequest;
import jakarta.validation.Valid;

public interface DeviceCreator {

  Device create(Long storeId, @Valid DeviceCreateRequest createRequest);

}
