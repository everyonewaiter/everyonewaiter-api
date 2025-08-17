package com.everyonewaiter.application.device;

import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.application.device.provided.DeviceManager;
import com.everyonewaiter.application.device.required.DeviceRepository;
import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.device.AlreadyUseDeviceNameException;
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

  private final Authenticator authenticator;
  private final StoreFinder storeFinder;
  private final DeviceRepository deviceRepository;

  @Override
  public Device create(Long storeId, DeviceCreateRequest createRequest) {
    validateDeviceCreate(storeId, createRequest);

    Store store = storeFinder.findOrThrow(storeId, new PhoneNumber(createRequest.phoneNumber()));

    Device device = switch (createRequest.purpose()) {
      case POS -> Device.createPos(store, createRequest);
      case HALL -> Device.createHall(store, createRequest);
      case TABLE -> Device.createTable(store, createRequest);
      case WAITING -> Device.createWaiting(store, createRequest);
    };

    return deviceRepository.save(device);
  }

  private void validateDeviceCreate(Long storeId, DeviceCreateRequest createRequest) {
    PhoneNumber phoneNumber = new PhoneNumber(createRequest.phoneNumber());

    authenticator.checkAuthSuccess(AuthPurpose.CREATE_DEVICE, phoneNumber);

    if (deviceRepository.exists(storeId, createRequest.name())) {
      throw new AlreadyUseDeviceNameException();
    }
  }

  @Override
  public Device update(Long deviceId, Long storeId, DeviceUpdateRequest updateRequest) {
    validateDeviceUpdate(deviceId, storeId, updateRequest);

    Device device = deviceRepository.findByIdAndStoreIdOrThrow(deviceId, storeId);

    switch (updateRequest.purpose()) {
      case POS -> device.updatePos(updateRequest);
      case HALL -> device.updateHall(updateRequest);
      case TABLE -> device.updateTable(updateRequest);
      case WAITING -> device.updateWaiting(updateRequest);
    }

    return deviceRepository.save(device);
  }

  private void validateDeviceUpdate(
      Long deviceId,
      Long storeId,
      DeviceUpdateRequest updateRequest
  ) {
    if (deviceRepository.existsExcludeId(deviceId, storeId, updateRequest.name())) {
      throw new AlreadyUseDeviceNameException();
    }
  }

  @Override
  public void delete(Long deviceId, Long storeId) {
    Device device = deviceRepository.findByIdAndStoreIdOrThrow(deviceId, storeId);

    device.delete();

    deviceRepository.delete(device);
  }

}
