package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.WaitingCountResponse;
import com.everyonewaiter.adapter.web.api.dto.WaitingDetailResponses;
import com.everyonewaiter.application.waiting.provided.WaitingAdministrator;
import com.everyonewaiter.application.waiting.provided.WaitingCustomer;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.store.StoreOpen;
import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingMyTurnView;
import com.everyonewaiter.domain.waiting.WaitingRegisterRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
class WaitingApi implements WaitingApiSpecification {

  private final WaitingCustomer waitingCustomer;
  private final WaitingAdministrator waitingAdministrator;

  @Override
  @GetMapping("/waitings")
  public ResponseEntity<WaitingDetailResponses> getWaitings(
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    List<Waiting> waitings = waitingAdministrator.findAll(device.getStoreId());

    return ResponseEntity.ok(WaitingDetailResponses.from(waitings));
  }

  @Override
  @GetMapping("/waitings/count")
  public ResponseEntity<WaitingCountResponse> count(
      @AuthenticationDevice(purpose = DevicePurpose.WAITING) Device device
  ) {
    int waitingCount = waitingAdministrator.getCount(device.getStoreId());

    return ResponseEntity.ok(WaitingCountResponse.from(waitingCount));
  }

  @Override
  @GetMapping("/stores/{storeId}/waitings/{accessKey}/my-turn")
  public ResponseEntity<WaitingMyTurnView> myTurn(
      @PathVariable Long storeId,
      @PathVariable String accessKey
  ) {
    return ResponseEntity.ok(waitingCustomer.getMyTurn(storeId, accessKey));
  }

  @Override
  @StoreOpen
  @PostMapping("/waitings")
  public ResponseEntity<Void> register(
      @RequestBody @Valid WaitingRegisterRequest registerRequest,
      @AuthenticationDevice(purpose = DevicePurpose.WAITING) Device device
  ) {
    Waiting waiting = waitingCustomer.register(device.getStoreId(), registerRequest);

    return ResponseEntity.created(URI.create(waiting.getNonNullId().toString())).build();
  }

  @Override
  @PostMapping("/waitings/{waitingId}/call")
  public ResponseEntity<Void> call(
      @PathVariable Long waitingId,
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    waitingAdministrator.customerCall(waitingId, device.getStoreId());

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/waitings/{waitingId}/complete")
  public ResponseEntity<Void> complete(
      @PathVariable Long waitingId,
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    waitingAdministrator.complete(waitingId, device.getStoreId());

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/waitings/{waitingId}/cancel")
  public ResponseEntity<Void> cancel(
      @PathVariable Long waitingId,
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    waitingAdministrator.cancel(waitingId, device.getStoreId());

    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/stores/{storeId}/waitings/{accessKey}/cancel")
  public ResponseEntity<Void> cancel(@PathVariable Long storeId, @PathVariable String accessKey) {
    waitingCustomer.cancel(storeId, accessKey);

    return ResponseEntity.noContent().build();
  }

}
