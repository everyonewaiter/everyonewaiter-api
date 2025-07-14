package com.everyonewaiter.domain.order.repository;

import com.everyonewaiter.domain.order.entity.OrderPayment;
import java.time.Instant;
import java.util.List;

public interface OrderPaymentRepository {

  List<OrderPayment> findAllByStoreIdAndDate(Long storeId, Instant start, Instant end);

  OrderPayment findByIdAndStoreId(Long orderPaymentId, Long storeId);

  OrderPayment save(OrderPayment orderPayment);

}
