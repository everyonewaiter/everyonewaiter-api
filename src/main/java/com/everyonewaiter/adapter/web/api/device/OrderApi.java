package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.OrderDetailResponses;
import com.everyonewaiter.application.order.provided.OrderFinder;
import com.everyonewaiter.application.order.provided.OrderServer;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderCreateRequest;
import com.everyonewaiter.domain.order.OrderType;
import com.everyonewaiter.domain.order.OrderView;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
class OrderApi implements OrderApiSpecification {

  private final OrderFinder orderFinder;
  private final OrderServer orderServer;

  @Override
  @GetMapping("/tables")
  public ResponseEntity<OrderDetailResponses> getOrdersByTable(
      @AuthenticationDevice(purpose = DevicePurpose.TABLE) Device device
  ) {
    var orders = orderFinder.findAllActive(device.getStoreId(), device.getTableNo());

    return ResponseEntity.ok(OrderDetailResponses.from(orders));
  }

  @Override
  @GetMapping("/hall")
  public ResponseEntity<OrderDetailResponses> getOrdersByHall(
      @RequestParam boolean served,
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    List<OrderView.OrderDetail> orders = orderFinder.findAll(device.getStoreId(), served);

    return ResponseEntity.ok(OrderDetailResponses.from(orders));
  }

  @Override
  @StoreOpen
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestBody @Valid OrderCreateRequest createRequest,
      @AuthenticationDevice(purpose = {DevicePurpose.TABLE, DevicePurpose.POS}) Device device
  ) {
    OrderType orderType = device.isPrepaid() ? OrderType.PREPAID : OrderType.POSTPAID;

    Order order = orderServer.create(device.getStoreId(), orderType, createRequest);

    return ResponseEntity.created(URI.create(String.valueOf(order.getId()))).build();
  }

  @Override
  @StoreOpen
  @PostMapping("/{orderId}/serving")
  public ResponseEntity<Void> servingOrder(
      @PathVariable Long orderId,
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    orderServer.serving(device.getStoreId(), orderId);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/{orderId}/menus/{orderMenuId}/serving")
  public ResponseEntity<Void> servingOrderMenu(
      @PathVariable Long orderId,
      @PathVariable Long orderMenuId,
      @AuthenticationDevice(purpose = DevicePurpose.HALL) Device device
  ) {
    orderServer.serving(device.getStoreId(), orderId, orderMenuId);

    return ResponseEntity.noContent().build();
  }

}
