package com.everyonewaiter.application.device.request;

import com.everyonewaiter.domain.device.entity.Device;

public class DeviceWrite {

  public record Create(
      String name,
      Device.Purpose purpose,
      int tableNo,
      String ksnetDeviceNo,
      Device.PaymentType paymentType
  ) {

  }

  public record Update(
      String name,
      Device.Purpose purpose,
      int tableNo,
      String ksnetDeviceNo,
      Device.PaymentType paymentType
  ) {

  }

}
