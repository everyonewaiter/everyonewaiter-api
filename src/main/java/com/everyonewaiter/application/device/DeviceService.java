package com.everyonewaiter.application.device;

import com.everyonewaiter.application.device.request.DeviceWrite;
import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.domain.device.service.DeviceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceService {

  private final DeviceValidator deviceValidator;
  private final DeviceRepository deviceRepository;

  @Transactional
  public DeviceResponse.Create create(Long storeId, DeviceWrite.Create request) {
    deviceValidator.validateUnique(storeId, request.name());

    Device device = switch (request.purpose()) {
      case POS -> {
        deviceValidator.validatePos(request.ksnetDeviceNo());
        yield Device.pos(storeId, request.name(), request.ksnetDeviceNo());
      }
      case HALL -> Device.hall(storeId, request.name());
      case TABLE -> {
        deviceValidator.validateTable(
            request.tableNo(),
            request.ksnetDeviceNo(),
            request.paymentType()
        );
        yield Device.table(
            storeId,
            request.name(),
            request.tableNo(),
            request.ksnetDeviceNo(),
            request.paymentType()
        );
      }
      case WAITING -> Device.waiting(storeId, request.name());
    };

    return DeviceResponse.Create.from(deviceRepository.save(device));
  }

}
