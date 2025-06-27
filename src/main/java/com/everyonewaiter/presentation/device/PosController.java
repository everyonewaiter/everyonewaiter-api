package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.pos.PosService;
import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.global.annotation.StoreOpen;
import com.everyonewaiter.presentation.device.request.PosWriteRequest;
import jakarta.validation.Valid;
import java.util.Optional;
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
@RequestMapping("/v1/pos")
class PosController implements PosControllerSpecification {

  private final PosService posService;

  @Override
  @StoreOpen
  @GetMapping("/tables")
  public ResponseEntity<PosResponse.Tables> getTables(
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    return ResponseEntity.ok(posService.readAllActiveTables(device.getStore().getId()));
  }

  @Override
  @StoreOpen
  @GetMapping("/tables/{tableNo}")
  public ResponseEntity<PosResponse.TableActivityDetail> getTableActivity(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = {Device.Purpose.TABLE, Device.Purpose.POS}) Device device
  ) {
    Optional<PosResponse.TableActivityDetail> response = posService.readActiveTable(
        device.getStore().getId(),
        device.hasPurpose(Device.Purpose.TABLE) ? device.getTableNo() : tableNo
    );
    return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/complete")
  public ResponseEntity<Void> completeActivity(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    posService.completeActivity(device.getStore().getId(), tableNo);
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{sourceTableNo}/move/{targetTableNo}")
  public ResponseEntity<Void> moveTable(
      @PathVariable int sourceTableNo,
      @PathVariable int targetTableNo,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    if (sourceTableNo != targetTableNo) {
      posService.moveTable(device.getStore().getId(), sourceTableNo, targetTableNo);
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/discount")
  public ResponseEntity<Void> discount(
      @PathVariable int tableNo,
      @RequestBody @Valid PosWriteRequest.Discount request,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    posService.discount(device.getStore().getId(), tableNo, request.discountPrice());
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/orders/{orderId}/cancel")
  public ResponseEntity<Void> cancelOrder(
      @PathVariable int tableNo,
      @PathVariable Long orderId,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    posService.cancelOrder(device.getStore().getId(), tableNo, orderId);
    return ResponseEntity.noContent().build();
  }

}
