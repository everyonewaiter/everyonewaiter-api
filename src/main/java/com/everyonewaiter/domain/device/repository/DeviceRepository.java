package com.everyonewaiter.domain.device.repository;

import com.everyonewaiter.domain.device.entity.Device;

public interface DeviceRepository {

  boolean existsByStoreIdAndName(Long storeId, String name);

  Device save(Device device);

}
