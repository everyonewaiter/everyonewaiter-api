package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.OrderPaymentDetailResponses;
import com.everyonewaiter.application.order.provided.OrderPaymentCreator;
import com.everyonewaiter.application.order.provided.OrderPaymentFinder;
import com.everyonewaiter.domain.auth.AuthenticationDevice;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.order.OrderPayment;
import com.everyonewaiter.domain.order.OrderPaymentApproveRequest;
import com.everyonewaiter.domain.order.OrderPaymentCancelRequest;
import com.everyonewaiter.domain.store.StoreOpen;
import com.everyonewaiter.domain.support.DateConverter;
import com.everyonewaiter.domain.support.TimeZone;
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
@RequestMapping("/v1/orders/payments")
class OrderPaymentApi implements OrderPaymentApiSpecification {

  private final OrderPaymentFinder orderPaymentFinder;
  private final OrderPaymentCreator orderPaymentCreator;

  @Override
  @GetMapping
  public ResponseEntity<OrderPaymentDetailResponses> getOrderPaymentsByPos(
      @RequestParam(value = "date", required = false) String date,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    List<OrderPayment> orderPayments = orderPaymentFinder.findAll(
        device.getStoreId(),
        DateConverter.convertToUtcStartInstant(TimeZone.ASIA_SEOUL, date),
        DateConverter.convertToUtcEndInstant(TimeZone.ASIA_SEOUL, date)
    );

    return ResponseEntity.ok(OrderPaymentDetailResponses.from(orderPayments));
  }

  @Override
  @StoreOpen
  @PostMapping("/{tableNo}/approve")
  public ResponseEntity<Void> approve(
      @PathVariable int tableNo,
      @RequestBody @Valid OrderPaymentApproveRequest approveRequest,
      @AuthenticationDevice(purpose = {DevicePurpose.TABLE, DevicePurpose.POS}) Device device
  ) {
    var payment = orderPaymentCreator.approve(device.getStoreId(), tableNo, approveRequest);

    return ResponseEntity.created(URI.create(String.valueOf(payment.getId()))).build();
  }

  @Override
  @StoreOpen
  @PostMapping("/{orderPaymentId}/cancel")
  public ResponseEntity<Void> cancel(
      @PathVariable Long orderPaymentId,
      @RequestBody @Valid OrderPaymentCancelRequest cancelRequest,
      @AuthenticationDevice(purpose = DevicePurpose.POS) Device device
  ) {
    var payment = orderPaymentCreator.cancel(device.getStoreId(), orderPaymentId, cancelRequest);

    return ResponseEntity.created(URI.create(String.valueOf(payment.getId()))).build();
  }

}
