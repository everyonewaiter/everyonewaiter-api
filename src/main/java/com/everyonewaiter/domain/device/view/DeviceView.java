package com.everyonewaiter.domain.device.view;

import com.everyonewaiter.domain.device.entity.Device;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceView {

  public record Page(
      Long id,
      Long storeId,
      String name,
      Device.Purpose purpose,
      Device.State state,
      Device.PaymentType paymentType,
      Instant createdAt,
      Instant updatedAt
  ) {

  }

}
