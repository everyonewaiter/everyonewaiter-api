package com.everyonewaiter.application.device;

import com.everyonewaiter.application.device.request.DeviceRead;
import com.everyonewaiter.application.device.request.DeviceWrite;
import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.domain.device.service.DeviceValidator;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceService {

  private final StoreRepository storeRepository;
  private final DeviceValidator deviceValidator;
  private final DeviceRepository deviceRepository;

  @Transactional
  public DeviceResponse.Create create(Long storeId, DeviceWrite.Create request) {
    Store store = storeRepository.findByIdOrThrow(storeId);
    deviceValidator.validateUnique(storeId, request.name());

    Device device = switch (request.purpose()) {
      case POS -> Device.pos(store, request.name());
      case HALL -> Device.hall(store, request.name());
      case TABLE -> {
        deviceValidator.validateTable(request.tableNo());
        yield Device.table(store, request.name(), request.tableNo(), request.paymentType());
      }
      case WAITING -> Device.waiting(store, request.name());
    };

    return DeviceResponse.Create.from(deviceRepository.save(device));
  }

  @Transactional
  public void update(Long deviceId, Long storeId, DeviceWrite.Update request) {
    Device device = deviceRepository.findByIdAndStoreIdOrThrow(deviceId, storeId);
    deviceValidator.validateUpdate(request.purpose(), request.tableNo());
    deviceValidator.validateUniqueExcludeId(deviceId, storeId, request.name());

    device.update(request.name(), request.purpose(), request.tableNo(), request.paymentType());
    deviceRepository.save(device);
  }

  @Transactional
  public void delete(Long deviceId, Long storeId) {
    Device device = deviceRepository.findByIdAndStoreIdOrThrow(deviceId, storeId);
    device.delete();
    deviceRepository.delete(device);
  }

  public Paging<DeviceResponse.PageView> readAll(Long storeId, DeviceRead.PageView request) {
    return deviceRepository.findAll(storeId, request.pagination())
        .map(DeviceResponse.PageView::from);
  }

  public DeviceResponse.Detail read(Long deviceId, Long storeId) {
    Device device = deviceRepository.findByIdAndStoreIdOrThrow(deviceId, storeId);
    return DeviceResponse.Detail.from(device);
  }

}
