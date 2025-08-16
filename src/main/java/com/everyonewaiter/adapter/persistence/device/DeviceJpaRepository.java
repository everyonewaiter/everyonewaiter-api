package com.everyonewaiter.adapter.persistence.device;

import com.everyonewaiter.domain.device.Device;
import org.springframework.data.jpa.repository.JpaRepository;

interface DeviceJpaRepository extends JpaRepository<Device, Long> {

  boolean existsByStoreIdAndName(Long storeId, String name);

}
