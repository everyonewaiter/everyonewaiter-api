package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.provided.PosTableActivityCreator;
import com.everyonewaiter.application.pos.provided.PosTableActivityFinder;
import com.everyonewaiter.domain.pos.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class PosTableActivityQueryService implements PosTableActivityFinder {

  private final PosTableActivityCreator posTableActivityCreator;
  private final PosTableActivityRepository posTableActivityRepository;

  @Override
  @Transactional
  public PosTableActivity findActiveOrCreate(Long storeId, int tableNo) {
    return posTableActivityRepository.findActive(storeId, tableNo)
        .orElseGet(() -> posTableActivityCreator.create(storeId, tableNo));
  }

}
