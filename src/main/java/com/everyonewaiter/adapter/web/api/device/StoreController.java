package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.application.store.provided.StoreManager;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePurpose;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
class StoreController implements StoreControllerSpecification {

  private final StoreManager storeManager;

  @Override
  @PostMapping("/open")
  public ResponseEntity<Void> open(
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    storeManager.open(device.getStore().getNonNullId());

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/close")
  public ResponseEntity<Void> close(
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    storeManager.close(device.getStore().getNonNullId());

    return ResponseEntity.noContent().build();
  }

}
