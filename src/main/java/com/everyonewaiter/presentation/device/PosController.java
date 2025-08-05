package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.pos.PosService;
import com.everyonewaiter.application.pos.response.PosResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.support.DateFormatter;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.global.annotation.StoreOpen;
import com.everyonewaiter.global.sse.ServerAction;
import com.everyonewaiter.global.sse.SseCategory;
import com.everyonewaiter.global.sse.SseEvent;
import com.everyonewaiter.global.sse.SseService;
import com.everyonewaiter.presentation.device.request.PosWriteRequest;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pos")
class PosController implements PosControllerSpecification {

  private final SseService sseService;
  private final PosService posService;

  @Override
  @GetMapping("/revenue")
  public ResponseEntity<PosResponse.Revenue> getRevenue(
      @RequestParam(value = "date", required = false) String date,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    PosResponse.Revenue response = posService.getRevenue(
        device.getStore().getId(),
        DateFormatter.kstDateStringToUtcStartInstant(date),
        DateFormatter.kstDateStringToUtcEndInstant(date)
    );
    return ResponseEntity.ok(response);
  }

  @Override
  @StoreOpen
  @GetMapping("/tables")
  public ResponseEntity<PosResponse.Tables> getTables(
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    return ResponseEntity.ok(posService.readAllActiveTables(device.getStore().getId()));
  }

  @Override
  @GetMapping("/tables/activities/{posTableActivityId}")
  public ResponseEntity<PosResponse.TableActivityDetail> getTableActivity(
      @PathVariable Long posTableActivityId,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    PosResponse.TableActivityDetail response = posService.readTableActivity(
        device.getStore().getId(),
        posTableActivityId
    );
    return ResponseEntity.ok(response);
  }

  @Override
  @StoreOpen
  @GetMapping("/tables/{tableNo}")
  public ResponseEntity<PosResponse.TableActivityDetail> getActiveTableActivity(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    Optional<PosResponse.TableActivityDetail> response = posService.readActiveTable(
        device.getStore().getId(),
        tableNo
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

  @Override
  @StoreOpen
  @PutMapping("/tables/{tableNo}/orders")
  public ResponseEntity<Void> updateOrders(
      @PathVariable int tableNo,
      @RequestBody @Valid PosWriteRequest.UpdateOrders request,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    Long storeId = Objects.requireNonNull(device.getStore().getId());
    Receipt receipt = posService.createDiffOrderReceipt(storeId, tableNo, request.toDomainDto());
    posService.updateOrders(device.getStore().getId(), tableNo, request.toDomainDto());
    if (!receipt.receiptMenus().isEmpty()) {
      sseService.sendEvent(storeId.toString(),
          new SseEvent(storeId, SseCategory.RECEIPT, ServerAction.UPDATE, receipt)
      );
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PutMapping("/tables/{tableNo}/orders/{orderId}/memo")
  public ResponseEntity<Void> updateMemo(
      @PathVariable int tableNo,
      @PathVariable Long orderId,
      @RequestBody @Valid PosWriteRequest.UpdateMemo request,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    posService.updateMemo(device.getStore().getId(), tableNo, orderId, request.memo());
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/resend-receipt")
  public ResponseEntity<Void> resendReceipt(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    posService.resendReceipt(device.getStore().getId(), tableNo);
    return ResponseEntity.noContent().build();
  }

}
