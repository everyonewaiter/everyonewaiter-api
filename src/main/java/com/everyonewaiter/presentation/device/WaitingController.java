package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.waiting.WaitingService;
import com.everyonewaiter.application.waiting.response.WaitingResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.presentation.device.request.WaitingWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class WaitingController implements WaitingControllerSpecification {

  private final WaitingService waitingService;

  @Override
  @GetMapping("/waitings")
  public ResponseEntity<WaitingResponse.Details> getWaitings(
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    return ResponseEntity.ok(waitingService.readAll(device.getStoreId()));
  }

  @Override
  @GetMapping("/waitings/count")
  public ResponseEntity<WaitingResponse.RegistrationCount> count(
      @AuthenticationDevice(purpose = Device.Purpose.WAITING) Device device
  ) {
    return ResponseEntity.ok(waitingService.getRegistrationCount(device.getStoreId()));
  }

  @Override
  @GetMapping("/stores/{storeId}/waitings/{accessKey}/my-turn")
  public ResponseEntity<WaitingResponse.MyTurn> myTurn(
      @PathVariable Long storeId,
      @PathVariable String accessKey
  ) {
    return ResponseEntity.ok(waitingService.getMyTurn(storeId, accessKey));
  }

  @Override
  @PostMapping("/waitings")
  public ResponseEntity<Void> create(
      @RequestBody @Valid WaitingWriteRequest.Create request,
      @AuthenticationDevice(purpose = Device.Purpose.WAITING) Device device
  ) {
    Long waitingId = waitingService.create(device.getStoreId(), request.toDomainDto());
    return ResponseEntity.created(URI.create(waitingId.toString())).build();
  }

  @Override
  @PostMapping("/waitings/{waitingId}/call")
  public ResponseEntity<Void> call(
      @PathVariable Long waitingId,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    waitingService.call(waitingId, device.getStoreId());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/waitings/{waitingId}/complete")
  public ResponseEntity<Void> complete(
      @PathVariable Long waitingId,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    waitingService.complete(waitingId, device.getStoreId());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/waitings/{waitingId}/cancel")
  public ResponseEntity<Void> cancel(
      @PathVariable Long waitingId,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    waitingService.cancel(waitingId, device.getStoreId());
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/stores/{storeId}/waitings/{accessKey}/cancel")
  public ResponseEntity<Void> cancel(@PathVariable Long storeId, @PathVariable String accessKey) {
    waitingService.cancel(storeId, accessKey);
    return ResponseEntity.noContent().build();
  }

}
