package com.everyonewaiter.application.order.required;

import com.everyonewaiter.domain.order.OrderPayment;
import java.time.Instant;
import java.util.List;

public interface OrderPaymentRepository {

  List<OrderPayment> findAll(Long storeId, Instant start, Instant end);

  OrderPayment findOrThrow(Long orderPaymentId, Long storeId);

  OrderPayment save(OrderPayment orderPayment);

}
