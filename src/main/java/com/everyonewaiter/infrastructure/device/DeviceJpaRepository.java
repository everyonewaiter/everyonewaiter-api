package com.everyonewaiter.infrastructure.device;

import com.everyonewaiter.domain.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

interface DeviceJpaRepository extends JpaRepository<Device, Long> {

  boolean existsByStoreIdAndName(Long storeId, String name);

}
