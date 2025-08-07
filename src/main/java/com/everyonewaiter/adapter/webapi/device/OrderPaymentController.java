package com.everyonewaiter.adapter.webapi.device;

import com.everyonewaiter.adapter.webapi.device.request.OrderPaymentWriteRequest;
import com.everyonewaiter.application.order.OrderPaymentService;
import com.everyonewaiter.application.order.response.OrderPaymentResponse;
import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.support.DateConverter;
import com.everyonewaiter.domain.support.TimeZone;
import com.everyonewaiter.global.annotation.AuthenticationDevice;
import com.everyonewaiter.global.annotation.StoreOpen;
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
@RequestMapping("/v1/orders/payments")
class OrderPaymentController implements OrderPaymentControllerSpecification {

  private final OrderPaymentService orderPaymentService;

  @Override
  @GetMapping
  public ResponseEntity<OrderPaymentResponse.Details> getOrderPaymentsByPos(
      @RequestParam(value = "date", required = false) String date,
      @AuthenticationDevice(purpose = Device.Purpose.POS) Device device
  ) {
    OrderPaymentResponse.Details response = orderPaymentService.readAllByPos(
        device.getStore().getId(),
        DateConverter.convertToUtcStartInstant(TimeZone.ASIA_SEOUL, date),
        DateConverter.convertToUtcEndInstant(TimeZone.ASIA_SEOUL, date)
    );
    return ResponseEntity.ok(response);
  }

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
