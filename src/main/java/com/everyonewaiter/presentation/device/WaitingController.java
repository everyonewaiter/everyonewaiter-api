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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/waitings")
class WaitingController implements WaitingControllerSpecification {

  private final WaitingService waitingService;

  @Override
  @GetMapping("/count")
  public ResponseEntity<WaitingResponse.RegistrationCount> count(
      @AuthenticationDevice(purpose = Device.Purpose.WAITING) Device device
  ) {
    return ResponseEntity.ok(waitingService.getRegistrationCount(device.getStoreId()));
  }

  @Override
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestBody @Valid WaitingWriteRequest.Create request,
      @AuthenticationDevice(purpose = Device.Purpose.WAITING) Device device
  ) {
    Long waitingId = waitingService.create(device.getStoreId(), request.toDomainDto());
    return ResponseEntity.created(URI.create(waitingId.toString())).build();
  }

}
