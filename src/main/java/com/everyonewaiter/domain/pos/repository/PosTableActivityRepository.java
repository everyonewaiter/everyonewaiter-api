package com.everyonewaiter.domain.pos.repository;

import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import java.util.Optional;

public interface PosTableActivityRepository {

  Optional<PosTableActivity> findByStoreIdAndTableNo(Long storeId, int tableNo);

  PosTableActivity findByStoreIdAndTableNoOrThrow(Long storeId, int tableNo);

  PosTableActivity save(PosTableActivity posTableActivity);

}
