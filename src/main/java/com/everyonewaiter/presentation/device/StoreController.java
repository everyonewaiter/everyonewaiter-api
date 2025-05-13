package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
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
    storeService.open(device.getStoreId());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/close")
  public ResponseEntity<Void> close(
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    storeService.close(device.getStoreId());
    return ResponseEntity.noContent().build();
  }

}
