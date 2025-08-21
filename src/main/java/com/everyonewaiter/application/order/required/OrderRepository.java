package com.everyonewaiter.application.order.required;

import com.everyonewaiter.domain.order.Order;
import java.util.List;

public interface OrderRepository {

  List<Order> findAll(Long storeId, boolean served);

  List<Order> findAllActive(Long storeId, int tableNo);

  Order findOrThrow(Long orderId);

  Order findOrThrow(Long orderId, Long storeId);

  Order save(Order order);

}
