package com.everyonewaiter.infrastructure.device;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class DeviceRepositoryImpl implements DeviceRepository {

  private final DeviceJpaRepository deviceJpaRepository;

  @Override
  public boolean existsByStoreIdAndName(Long storeId, String name) {
    return deviceJpaRepository.existsByStoreIdAndName(storeId, name);
  }

  @Override
  public Device save(Device device) {
    return deviceJpaRepository.save(device);
  }

}
