package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.DeviceDetailResponse;
import com.everyonewaiter.application.device.provided.DeviceFinder;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class DeviceApi implements DeviceApiSpecification {

  private final DeviceFinder deviceFinder;

  @Override
  @GetMapping("/devices/{deviceId}")
  public ResponseEntity<DeviceDetailResponse> getDevice(@PathVariable Long deviceId) {
    Device device = deviceFinder.findOrThrow(deviceId);

    return ResponseEntity.ok(DeviceDetailResponse.from(device));
  }

  @Override
  @GetMapping("/devices")
  public ResponseEntity<DeviceDetailResponse> getDevice(@AuthenticationDevice Device device) {
    return ResponseEntity.ok(DeviceDetailResponse.from(device));
  }

}
