package com.everyonewaiter.application.pos.provided;

import com.everyonewaiter.domain.order.OrderMemoUpdateRequest;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.pos.PosTable;
import jakarta.validation.Valid;

public interface PosTableOrderManager {

  PosTable cancel(Long storeId, int tableNo, Long orderId);

  PosTable update(Long storeId, int tableNo, @Valid OrderUpdateRequests updateRequests);

  PosTable update(
      Long storeId,
      int tableNo,
      Long orderId,
      @Valid OrderMemoUpdateRequest updateRequest
  );

}
