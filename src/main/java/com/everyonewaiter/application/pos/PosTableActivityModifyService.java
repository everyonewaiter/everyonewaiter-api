package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.provided.PosTableActivityCreator;
import com.everyonewaiter.application.pos.provided.PosTableFinder;
import com.everyonewaiter.application.pos.required.PosTableActivityRepository;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class PosTableActivityModifyService implements PosTableActivityCreator {

  private final PosTableFinder posTableFinder;
  private final PosTableActivityRepository posTableActivityRepository;

  @Override
  public PosTableActivity create(Long storeId, int tableNo) {
    PosTable posTable = posTableFinder.findActiveOrThrow(storeId, tableNo);

    PosTableActivity posTableActivity = PosTableActivity.create(posTable);

    return posTableActivityRepository.save(posTableActivity);
  }

}
