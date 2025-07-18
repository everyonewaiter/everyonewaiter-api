package com.everyonewaiter.domain.pos.repository;

import com.everyonewaiter.domain.order.entity.OrderPayment;
import com.everyonewaiter.domain.pos.entity.PosTableActivity;
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

  PosTableActivity findByIdAndStoreIdOrThrow(Long posTableActivityId, Long storeId);

  Optional<PosTableActivity> findByStoreIdAndTableNo(Long storeId, int tableNo);

  PosTableActivity findByStoreIdAndTableNoOrThrow(Long storeId, int tableNo);

  PosTableActivity save(PosTableActivity posTableActivity);

}
