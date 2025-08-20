package com.everyonewaiter.domain.pos.repository;

import com.everyonewaiter.domain.order.entity.OrderPayment;
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
      OrderPayment.Method method,
      OrderPayment.State state
  );

  Optional<PosTableActivity> findActive(Long storeId, int tableNo);

  PosTableActivity findActiveOrThrow(Long storeId, int tableNo);

  PosTableActivity findOrThrow(Long posTableActivityId, Long storeId);

  PosTableActivity save(PosTableActivity posTableActivity);

}
