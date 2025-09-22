package com.everyonewaiter.application.pos.provided;

import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.PosView;
import java.time.Instant;
import java.util.Optional;

public interface PosTableActivityFinder {

  PosView.Revenue getRevenue(Long storeId, Instant start, Instant end);

  Optional<PosTableActivity> findActive(Long storeId, int tableNo);

  PosTableActivity findActiveOrThrow(Long storeId, int tableNo);

  PosView.PosTableActivityDetail findOrThrow(Long storeId, Long posTableActivityId);

}
