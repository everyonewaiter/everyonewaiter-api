package com.everyonewaiter.application.order.provided;

import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderCreateRequest;
import com.everyonewaiter.domain.order.OrderType;
import jakarta.validation.Valid;

public interface OrderServer {

  Order create(Long storeId, OrderType orderType, @Valid OrderCreateRequest createRequest);

  Order serving(Long storeId, int tableNo, Long orderId);

  Order serving(Long storeId, int tableNo, Long orderId, Long orderMenuId);

}
