package com.everyonewaiter.application.receipt.provided;

import com.everyonewaiter.domain.order.Order;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.receipt.Receipt;
import jakarta.validation.Valid;
import java.util.List;

public interface ReceiptCreator {

  Receipt create(Long storeId, int tableNo, Long orderId);

  Receipt create(Long storeId, int tableNo, List<Long> orderIds);

  Receipt createDiff(
      Long storeId,
      int tableNo,
      List<Order> orders,
      @Valid OrderUpdateRequests updateRequests
  );

  Receipt createCancel(Long storeId, int tableNo, Long orderId);

}
