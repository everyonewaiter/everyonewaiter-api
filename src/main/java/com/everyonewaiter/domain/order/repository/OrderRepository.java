package com.everyonewaiter.domain.order.repository;

import com.everyonewaiter.domain.order.entity.Order;
import java.util.List;

public interface OrderRepository {

  List<Order> findAllActiveByStoreIdAndTableNo(Long storeId, int tableNo);

  List<Order> findAllByStoreIdAndServed(Long storeId, boolean served);

  Order save(Order order);

  Order findByIdOrThrow(Long orderId);

  Order findByIdAndStoreIdOrThrow(Long orderId, Long storeId);

}
