package com.everyonewaiter.application.device.response;

import com.everyonewaiter.domain.device.entity.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceResponse {

  @Schema(name = "DeviceResponse.Create")
  public record Create(
      @Schema(description = "기기 ID", example = "\"694865267482835533\"")
      String deviceId,

      @Schema(description = "비밀키(이후 조회 불가)", example = "0KJEC3J6QF7TW")
      String secretKey
  ) {

    public static Create from(Device device) {
      return new Create(Objects.requireNonNull(device.getId()).toString(), device.getSecretKey());
    }

  }

}
