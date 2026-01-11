package com.everyonewaiter.application.device;

import com.everyonewaiter.application.device.provided.DeviceFinder;
import com.everyonewaiter.application.device.provided.DeviceManager;
import com.everyonewaiter.application.device.provided.DeviceValidator;
import com.everyonewaiter.application.device.required.DeviceRepository;
import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DeviceCreateRequest;
import com.everyonewaiter.domain.device.DeviceUpdateRequest;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class DeviceModifyService implements DeviceManager {

  private final StoreFinder storeFinder;
  private final DeviceFinder deviceFinder;
  private final DeviceValidator deviceValidator;
  private final DeviceRepository deviceRepository;

  @Override
  public Device create(Long storeId, DeviceCreateRequest createRequest) {
    deviceValidator.checkDuplicateName(storeId, createRequest.name());

    Store store = storeFinder.findOrThrow(storeId, new PhoneNumber(createRequest.phoneNumber()));

    Device device = Device.create(store, createRequest);

    return deviceRepository.save(device);
  }

  @Override
  public Device update(Long deviceId, Long storeId, DeviceUpdateRequest updateRequest) {
    deviceValidator.checkDuplicateNameExcludeId(deviceId, storeId, updateRequest.name());

    Device device = deviceFinder.findOrThrow(deviceId, storeId);

    device.update(updateRequest);

    return deviceRepository.save(device);
  }

  @Override
  public void delete(Long deviceId, Long storeId) {
    Device device = deviceFinder.findOrThrow(deviceId, storeId);

    device.delete();

    deviceRepository.delete(device);
  }

}
