package com.everyonewaiter.domain.device.service;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.global.exception.AccessDeniedException;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceValidator {

  private final DeviceRepository deviceRepository;

  public void validateUnique(Long storeId, String name) {
    if (deviceRepository.existsByStoreIdAndName(storeId, name)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_DEVICE_NAME);
    }
  }

  public void validateUniqueExcludeId(Long deviceId, Long storeId, String name) {
    if (deviceRepository.existsByStoreIdAndNameExcludeId(deviceId, storeId, name)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_DEVICE_NAME);
    }
  }

  public void validateUpdate(Device.Purpose purpose, int tableNo) {
    if (purpose == Device.Purpose.TABLE) {
      validateTable(tableNo);
    }
  }

  public void validateTable(int tableNo) {
    if (tableNo < 1) {
      throw new IllegalArgumentException("테이블 번호는 1 이상으로 입력해 주세요.");
    }
    if (tableNo > 100) {
      throw new IllegalArgumentException("테이블 번호는 100 이하로 입력해 주세요.");
    }
  }

  public void validateDevicePurpose(Device device, Device.Purpose[] purpose) {
    if (!device.isActive() || Arrays.stream(purpose).noneMatch(device::hasPurpose)) {
      throw new AccessDeniedException();
    }
  }

}
