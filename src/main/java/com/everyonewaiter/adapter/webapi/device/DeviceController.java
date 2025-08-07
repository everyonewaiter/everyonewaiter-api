package com.everyonewaiter.adapter.webapi.device;

import com.everyonewaiter.application.device.DeviceService;
import com.everyonewaiter.application.device.response.DeviceResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class DeviceController implements DeviceControllerSpecification {

  private final DeviceService deviceService;

  @Override
  @GetMapping("/devices")
  public ResponseEntity<DeviceResponse.Detail> getDevice(@AuthenticationDevice Device device) {
    return ResponseEntity.ok(deviceService.read(device.getId(), device.getStore().getId()));
  }

}
