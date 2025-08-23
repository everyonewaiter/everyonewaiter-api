package com.everyonewaiter.application.receipt.provided;

import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.receipt.Receipt;
import java.util.List;

public interface ReceiptCreator {

  Receipt create(Long storeId, Long orderId);

  Receipt create(Long storeId, List<Long> orderIds);

  Receipt createDiff(Long storeId, List<Order> orders, OrderUpdateRequests updateRequests);

}
