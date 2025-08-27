package com.everyonewaiter.application.device.provided;

import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePageRequest;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.device.DeviceState;
import com.everyonewaiter.domain.shared.Paging;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface DeviceFinder {

  List<Device> findAll(Long storeId, DevicePurpose purpose, DeviceState state);

  Paging<Device> findAll(Long storeId, @Valid DevicePageRequest pageRequest);

  Optional<Device> find(Long deviceId);

  Device findOrThrow(Long deviceId);

  Device findOrThrow(Long deviceId, Long storeId);

}
