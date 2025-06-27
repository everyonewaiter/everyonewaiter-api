package com.everyonewaiter.presentation.device;

import com.everyonewaiter.application.order.OrderPaymentService;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.global.annotation.StoreOpen;
import com.everyonewaiter.presentation.device.request.OrderPaymentWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders/payments")
class OrderPaymentController implements OrderPaymentControllerSpecification {

  private final OrderPaymentService orderPaymentService;

  @Override
  @StoreOpen
  @PostMapping("/{tableNo}/approve")
  public ResponseEntity<Void> approve(
      @PathVariable int tableNo,
      @RequestBody @Valid OrderPaymentWriteRequest.Approve request,
      @AuthenticationDevice(purpose = {Device.Purpose.TABLE, Device.Purpose.POS}) Device device
  ) {
    Long paymentId = orderPaymentService.approve(
        device.getStore().getId(),
        tableNo,
        request.toDomainDto()
    );
    return ResponseEntity.created(URI.create(paymentId.toString())).build();
  }

  @Override
  @StoreOpen
  @PostMapping("/{orderPaymentId}/cancel")
  public ResponseEntity<Void> cancel(
      @PathVariable Long orderPaymentId,
      @RequestBody @Valid OrderPaymentWriteRequest.Cancel request,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    Long paymentId = orderPaymentService.cancel(
        device.getStore().getId(),
        orderPaymentId,
        request.toDomainDto()
    );
    return ResponseEntity.created(URI.create(paymentId.toString())).build();
  }

}
