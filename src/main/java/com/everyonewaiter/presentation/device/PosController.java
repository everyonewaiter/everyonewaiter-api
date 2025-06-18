package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.pos.PosService;
import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.global.annotation.StoreOpen;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    return ResponseEntity.ok(posService.readAllActiveTables(device.getStoreId()));
  }

  @Override
  @StoreOpen
  @GetMapping("/tables/{tableNo}")
  public ResponseEntity<PosResponse.TableActivityDetail> getTableActivity(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = {Device.Purpose.TABLE, Device.Purpose.POS}) Device device
  ) {
    Optional<PosResponse.TableActivityDetail> response = posService.readActiveTable(
        device.getStoreId(),
        device.hasPurpose(Device.Purpose.TABLE) ? device.getTableNo() : tableNo
    );
    return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }

}
