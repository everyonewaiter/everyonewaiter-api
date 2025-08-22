package com.everyonewaiter.domain.pos.repository;

import com.everyonewaiter.domain.order.OrderPaymentMethod;
import com.everyonewaiter.domain.order.OrderPaymentState;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.view.PosTableActivityView;
import java.time.Instant;
import java.util.Optional;

public interface PosTableActivityRepository {

  PosTableActivityView.TotalRevenue getTotalRevenue(Long storeId, Instant start, Instant end);

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
