package com.everyonewaiter.application.order.provided;

import com.everyonewaiter.domain.order.OrderPayment;
import java.time.Instant;
import java.util.List;

public interface OrderPaymentFinder {

  List<OrderPayment> findAll(Long storeId, Instant start, Instant end);

  OrderPayment findOrThrow(Long orderPaymentId, Long storeId);

}
