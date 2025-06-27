package com.everyonewaiter.application.device;

import com.everyonewaiter.application.device.request.DeviceRead;
import com.everyonewaiter.application.device.request.DeviceWrite;
import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.domain.device.service.DeviceValidator;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.global.support.Paging;
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
      case POS -> {
        deviceValidator.validatePos(request.ksnetDeviceNo());
        yield Device.pos(store, request.name(), request.ksnetDeviceNo());
      }
      case HALL -> Device.hall(store, request.name());
      case TABLE -> {
        deviceValidator.validateTable(
            request.tableNo(),
            request.ksnetDeviceNo(),
            request.paymentType()
        );
        yield Device.table(
            store,
            request.name(),
            request.tableNo(),
            request.ksnetDeviceNo(),
            request.paymentType()
        );
      }
      case WAITING -> Device.waiting(store, request.name());
    };

    return DeviceResponse.Create.from(deviceRepository.save(device));
  }

  @Transactional
  public void update(Long deviceId, Long storeId, DeviceWrite.Update request) {
    Device device = deviceRepository.findByIdAndStoreIdOrThrow(deviceId, storeId);
    deviceValidator.validateUniqueExcludeId(deviceId, storeId, request.name());

    switch (request.purpose()) {
      case POS -> deviceValidator.validatePos(request.ksnetDeviceNo());
      case TABLE -> deviceValidator.validateTable(
          request.tableNo(),
          request.ksnetDeviceNo(),
          request.paymentType()
      );
      default -> {
        // No validation needed for other purposes
      }
    }

    device.update(
        request.name(),
        request.purpose(),
        request.tableNo(),
        request.ksnetDeviceNo(),
        request.paymentType()
    );
    deviceRepository.save(device);
  }

  @Transactional
  public void delete(Long deviceId, Long storeId) {
    Device device = deviceRepository.findByIdAndStoreIdOrThrow(deviceId, storeId);
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
