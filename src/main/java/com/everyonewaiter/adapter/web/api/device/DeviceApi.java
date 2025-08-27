package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.DeviceDetailResponse;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class DeviceApi implements DeviceApiSpecification {

  @Override
  @GetMapping("/devices")
  public ResponseEntity<DeviceDetailResponse> getDevice(@AuthenticationDevice Device device) {
    return ResponseEntity.ok(DeviceDetailResponse.from(device));
  }

}
