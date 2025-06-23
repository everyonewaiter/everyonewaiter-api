package com.everyonewaiter.domain.order.repository;

import com.everyonewaiter.domain.order.entity.OrderPayment;

public interface OrderPaymentRepository {

  OrderPayment findByIdAndStoreId(Long orderPaymentId, Long storeId);

  OrderPayment save(OrderPayment orderPayment);

}
