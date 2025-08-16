package com.everyonewaiter.application.device.required;

import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePageRequest;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.device.DeviceState;
import com.everyonewaiter.domain.shared.Paging;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository {

  boolean exists(Long storeId, String name);

  boolean existsExcludeId(Long deviceId, Long storeId, String name);

  Paging<Device> findAll(Long storeId, DevicePageRequest pageRequest);

  List<Device> findAll(Long storeId, DevicePurpose purpose, DeviceState state);

  Optional<Device> findById(Long deviceId);

  Device findByIdAndStoreIdOrThrow(Long deviceId, Long storeId);

  Device save(Device device);

  void delete(Device device);

}
