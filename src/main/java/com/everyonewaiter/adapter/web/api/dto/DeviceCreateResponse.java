package com.everyonewaiter.adapter.web.api.dto;

import com.everyonewaiter.domain.device.Device;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DeviceCreateResponse")
public record DeviceCreateResponse(
    @Schema(description = "기기 ID", example = "\"694865267482835533\"")
    String deviceId,

    @Schema(description = "비밀키(이후 조회 불가)", example = "0KJEC3J6QF7TW")
    String secretKey
) {

  public static DeviceCreateResponse from(Device device) {
    return new DeviceCreateResponse(String.valueOf(device.getId()), device.getSecretKey());
  }

}
