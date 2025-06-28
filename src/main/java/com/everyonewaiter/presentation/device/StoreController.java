package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
class StoreController implements StoreControllerSpecification {

  private final StoreService storeService;

  @Override
  @GetMapping("/status")
  public ResponseEntity<StoreResponse.SimpleWithStatus> getStore(
      @AuthenticationDevice Device device
  ) {
    return ResponseEntity.ok(storeService.readSimpleWithStatusView(device.getStore().getId()));
  }

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
