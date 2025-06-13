package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.order.OrderService;
import com.everyonewaiter.application.order.response.OrderResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.order.entity.Order;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.global.annotation.StoreOpen;
import com.everyonewaiter.presentation.device.request.OrderWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @Override
  @StoreOpen
  @GetMapping("/hall")
  public ResponseEntity<OrderResponse.Details> getOrdersByHall(
      @RequestParam boolean served,
      @AuthenticationDevice(purpose = Device.Purpose.HALL) Device device
  ) {
    return ResponseEntity.ok(orderService.readAllByHall(device.getStoreId(), served));
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
        device.getStoreId(),
        request.tableNo(),
        request.toDomainDto(orderType)
    );
    return ResponseEntity.created(URI.create(orderId.toString())).build();
  }

}
