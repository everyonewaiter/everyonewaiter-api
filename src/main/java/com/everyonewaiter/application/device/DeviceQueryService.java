package com.everyonewaiter.application.device;

import com.everyonewaiter.application.device.provided.DeviceFinder;
import com.everyonewaiter.application.device.required.DeviceRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePageRequest;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.device.DeviceState;
import com.everyonewaiter.domain.shared.Paging;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class DeviceQueryService implements DeviceFinder {

  private final DeviceRepository deviceRepository;

  @Override
  public List<Device> findAll(Long storeId, DevicePurpose purpose, DeviceState state) {
    return deviceRepository.findAll(storeId, purpose, state);
  }

  @Override
  public Paging<Device> findAll(Long storeId, DevicePageRequest pageRequest) {
    return deviceRepository.findAll(storeId, pageRequest);
  }

  @Override
  public Optional<Device> find(Long deviceId) {
    return deviceRepository.findById(deviceId);
  }

  @Override
  public Device findOrThrow(Long deviceId, Long storeId) {
    return deviceRepository.findOrThrow(deviceId, storeId);
  }

}
