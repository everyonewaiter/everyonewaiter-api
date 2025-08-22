package com.everyonewaiter.application.order.provided;

import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderView;
import java.util.List;

public interface OrderFinder {

  List<OrderView.OrderDetail> findAll(Long storeId, boolean served);

  List<OrderView.OrderDetail> findAllActive(Long storeId, int tableNo);

  Order findOrThrow(Long orderId);

  Order findOrThrow(Long orderId, Long storeId);

}
