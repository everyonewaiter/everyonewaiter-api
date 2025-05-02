package com.everyonewaiter.domain.device.service;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
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

  public void validatePos(String ksnetDeviceNo) {
    validateKsnetDeviceNo(ksnetDeviceNo);
  }

  public void validateTable(
      int tableNo,
      String ksnetDeviceNo,
      Device.PaymentType paymentType
  ) {
    validateTableNo(tableNo);
    if (paymentType == Device.PaymentType.PREPAID) {
      validateKsnetDeviceNo(ksnetDeviceNo);
    }
  }

  private void validateKsnetDeviceNo(String ksnetDeviceNo) {
    if (ksnetDeviceNo.isBlank() || ksnetDeviceNo.length() < 8) {
      throw new IllegalArgumentException("KSNET 단말기 번호를 입력해 주세요.");
    }
    if (ksnetDeviceNo.length() > 30) {
      throw new IllegalArgumentException("옳바른 KSNET 단말기 번호를 입력해 주세요.");
    }
  }

  private void validateTableNo(int tableNo) {
    if (tableNo < 1) {
      throw new IllegalArgumentException("테이블 번호는 1 이상으로 입력해 주세요.");
    }
    if (tableNo > 100) {
      throw new IllegalArgumentException("테이블 번호는 100 이하로 입력해 주세요.");
    }
  }

}
