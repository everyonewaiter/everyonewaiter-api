package com.everyonewaiter.domain.order.repository;

import com.everyonewaiter.domain.order.entity.Order;

public interface OrderRepository {

  Order save(Order order);

}
