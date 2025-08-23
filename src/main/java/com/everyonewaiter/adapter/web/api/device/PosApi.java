package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.PosTableDetailResponses;
import com.everyonewaiter.application.pos.PosService;
import com.everyonewaiter.application.pos.provided.PosTableActivityFinder;
import com.everyonewaiter.application.pos.provided.PosTableFinder;
import com.everyonewaiter.application.pos.provided.PosTableManager;
import com.everyonewaiter.application.pos.provided.PosTableOrderManager;
import com.everyonewaiter.application.sse.provided.SseSender;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.order.OrderMemoUpdateRequest;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.pos.PosTableDiscountRequest;
import com.everyonewaiter.domain.pos.PosView;
import com.everyonewaiter.domain.store.StoreOpen;
import com.everyonewaiter.domain.support.DateConverter;
import com.everyonewaiter.domain.support.TimeZone;
import jakarta.validation.Valid;
import java.util.List;
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
class PosApi implements PosApiSpecification {

  private final SseSender sseSender;
  private final PosService posService;
  private final PosTableFinder posTableFinder;
  private final PosTableActivityFinder posTableActivityFinder;
  private final PosTableManager posTableManager;
  private final PosTableOrderManager posTableOrderManager;

  @Override
  @GetMapping("/revenue")
  public ResponseEntity<PosView.Revenue> getRevenue(
      @RequestParam(value = "date", required = false) String date,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    PosView.Revenue revenue = posTableActivityFinder.getRevenue(
        device.getStoreId(),
        DateConverter.convertToUtcStartInstant(TimeZone.ASIA_SEOUL, date),
        DateConverter.convertToUtcEndInstant(TimeZone.ASIA_SEOUL, date)
    );

    return ResponseEntity.ok(revenue);
  }

  @Override
  @StoreOpen
  @GetMapping("/tables")
  public ResponseEntity<PosTableDetailResponses> getTables(
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    List<PosView.PosTableDetail> posTables = posTableFinder.findAllActive(device.getStoreId());

    return ResponseEntity.ok(PosTableDetailResponses.from(posTables));
  }

  @Override
  @GetMapping("/tables/activities/{posTableActivityId}")
  public ResponseEntity<PosView.PosTableActivityDetail> getTableActivity(
      @PathVariable Long posTableActivityId,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    return ResponseEntity.ok(
        posTableActivityFinder.findOrThrow(device.getStoreId(), posTableActivityId)
    );
  }

  @Override
  @StoreOpen
  @GetMapping("/tables/{tableNo}")
  public ResponseEntity<PosView.PosTableActivityDetail> getActiveTableActivity(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    return posTableFinder.findActiveActivity(device.getStoreId(), tableNo)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{sourceTableNo}/move/{targetTableNo}")
  public ResponseEntity<Void> moveTable(
      @PathVariable int sourceTableNo,
      @PathVariable int targetTableNo,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    posTableManager.moveTable(device.getStoreId(), sourceTableNo, targetTableNo);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/discount")
  public ResponseEntity<Void> discount(
      @PathVariable int tableNo,
      @RequestBody @Valid PosTableDiscountRequest discountRequest,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    posTableManager.discount(device.getStoreId(), tableNo, discountRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/complete")
  public ResponseEntity<Void> completeActivity(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    posTableManager.completeActivity(device.getStoreId(), tableNo);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/orders/{orderId}/cancel")
  public ResponseEntity<Void> cancelOrder(
      @PathVariable int tableNo,
      @PathVariable Long orderId,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    posTableOrderManager.cancel(device.getStoreId(), tableNo, orderId);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PutMapping("/tables/{tableNo}/orders")
  public ResponseEntity<Void> updateOrders(
      @PathVariable int tableNo,
      @RequestBody @Valid OrderUpdateRequests updateRequests,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    posTableOrderManager.update(device.getStoreId(), tableNo, updateRequests);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PutMapping("/tables/{tableNo}/orders/{orderId}/memo")
  public ResponseEntity<Void> updateOrderMemo(
      @PathVariable int tableNo,
      @PathVariable Long orderId,
      @RequestBody @Valid OrderMemoUpdateRequest updateRequest,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    posTableOrderManager.update(device.getStoreId(), tableNo, orderId, updateRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/tables/{tableNo}/resend-receipt")
  public ResponseEntity<Void> resendReceipt(
      @PathVariable int tableNo,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    posService.resendReceipt(device.getStoreId(), tableNo);
    return ResponseEntity.noContent().build();
  }

}
