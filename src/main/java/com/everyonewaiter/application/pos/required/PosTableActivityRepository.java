package com.everyonewaiter.application.pos.required;

import com.everyonewaiter.domain.order.OrderPaymentMethod;
import com.everyonewaiter.domain.order.OrderPaymentState;
import com.everyonewaiter.domain.order.OrderState;
import com.everyonewaiter.domain.pos.PosTableActivity;
import java.time.Instant;
import java.util.Optional;

public interface PosTableActivityRepository {

  boolean existsActive(Long storeId);

  long getDiscountRevenue(Long storeId, Instant start, Instant end);

  long getOrderRevenue(Long storeId, Instant start, Instant end, OrderState state);

  long getPaymentRevenue(
      Long storeId,
      Instant start,
      Instant end,
      OrderPaymentMethod method,
      OrderPaymentState state
  );

  Optional<PosTableActivity> findActive(Long storeId, int tableNo);

  PosTableActivity findActiveOrThrow(Long storeId, int tableNo);

  PosTableActivity findOrThrow(Long posTableActivityId, Long storeId);

  PosTableActivity save(PosTableActivity posTableActivity);

}
