package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.entity.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
class StoreController implements StoreControllerSpecification {

  private final StoreService storeService;

  @Override
  @PostMapping("/open")
  public ResponseEntity<Void> open(
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    storeService.open(device.getStore().getId());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/close")
  public ResponseEntity<Void> close(
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    storeService.close(device.getStore().getId());
    return ResponseEntity.noContent().build();
  }

}
