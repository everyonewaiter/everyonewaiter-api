package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.device.request.OrderWriteRequest;
import com.everyonewaiter.application.order.OrderService;
import com.everyonewaiter.application.order.StaffCallService;
import com.everyonewaiter.application.order.response.OrderResponse;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.domain.store.StoreOpen;
import jakarta.validation.Valid;
import java.net.URI;
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
class OrderController implements OrderControllerSpecification {

  private final OrderService orderService;
  private final StaffCallService staffCallService;

  @Override
  @GetMapping("/tables")
  public ResponseEntity<OrderResponse.Details> getOrdersByTable(
      @AuthenticationDevice(purpose = Device.Purpose.TABLE) Device device
  ) {
    OrderResponse.Details response = orderService.readAllByTable(
        device.getStore().getId(),
        device.getTableNo()
    );
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/hall")
  public ResponseEntity<OrderResponse.Details> getOrdersByHall(
      @RequestParam boolean served,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    return ResponseEntity.ok(orderService.readAllByHall(device.getStore().getId(), served));
  }

  @Override
  @StoreOpen
  @PostMapping
  public ResponseEntity<Void> create(
      @RequestBody @Valid OrderWriteRequest.Create request,
      @AuthenticationDevice(purpose = {Device.Purpose.TABLE, Device.Purpose.POS}) Device device
  ) {
    Order.Type orderType = device.isPrepaid() ? Order.Type.PREPAID : Order.Type.POSTPAID;
    Long orderId = orderService.createOrder(
        device.getStore().getId(),
        request.tableNo(),
        request.toDomainDto(orderType)
    );
    return ResponseEntity.created(URI.create(orderId.toString())).build();
  }

  @Override
  @StoreOpen
  @PostMapping("/{orderId}/serving")
  public ResponseEntity<Void> servingOrder(
      @PathVariable Long orderId,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    orderService.servingOrder(device.getStore().getId(), orderId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/{orderId}/menus/{orderMenuId}/serving")
  public ResponseEntity<Void> servingOrderMenu(
      @PathVariable Long orderId,
      @PathVariable Long orderMenuId,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    orderService.servingOrderMenu(device.getStore().getId(), orderId, orderMenuId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/staff-calls")
  public ResponseEntity<OrderResponse.StaffCallDetails> getStaffCalls(
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    return ResponseEntity.ok(staffCallService.readAllIncomplete(device.getStore().getId()));
  }

  @Override
  @StoreOpen
  @PostMapping("/staff-calls/{staffCallId}/complete")
  public ResponseEntity<Void> completeStaffCall(
      @PathVariable Long staffCallId,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    staffCallService.complete(device.getStore().getId(), staffCallId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOpen
  @PostMapping("/staff-calls")
  public ResponseEntity<Void> callStaff(
      @RequestBody @Valid OrderWriteRequest.StaffCallOption request,
      @AuthenticationDevice(purpose = Device.Purpose.TABLE) Device device
  ) {
    Long staffCallId = staffCallService.callStaff(
        device.getStore().getId(),
        device.getTableNo(),
        request.optionName()
    );
    return ResponseEntity.created(URI.create(staffCallId.toString())).build();
  }

}
