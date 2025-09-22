package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.StaffCallDetailResponses;
import com.everyonewaiter.application.staffcall.provided.StaffCallManager;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.staffcall.StaffCall;
import com.everyonewaiter.domain.staffcall.StaffCallRequest;
import com.everyonewaiter.domain.store.StoreOpen;
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
class StaffCallApi implements StaffCallApiSpecification {

  private final StaffCallManager staffCallManager;

  @Override
  @GetMapping("/orders/staff-calls")
  public ResponseEntity<StaffCallDetailResponses> getStaffCalls(
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    List<StaffCall> staffCalls = staffCallManager.findAllIncompleted(device.getStoreId());

    return ResponseEntity.ok(StaffCallDetailResponses.from(staffCalls));
  }

  @Override
  @StoreOpen
  @PostMapping("/orders/staff-calls")
  public ResponseEntity<Void> call(
      @RequestBody @Valid StaffCallRequest callRequest,
      @AuthenticationDevice(purpose = DevicePurpose.TABLE) Device device
  ) {
    StaffCall staffCall =
        staffCallManager.call(device.getStoreId(), device.getTableNo(), callRequest);

    return ResponseEntity.created(URI.create(String.valueOf(staffCall.getId()))).build();
  }

  @Override
  @StoreOpen
  @PostMapping("/orders/staff-calls/{staffCallId}/complete")
  public ResponseEntity<Void> complete(
      @PathVariable Long staffCallId,
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    staffCallManager.complete(device.getStoreId(), staffCallId);

    return ResponseEntity.noContent().build();
  }

}
