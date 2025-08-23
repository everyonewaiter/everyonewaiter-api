package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.provided.PosTableActivityCreator;
import com.everyonewaiter.application.pos.required.PosTableActivityRepository;
import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class PosTableActivityModifyService implements PosTableActivityCreator {

  private final PosTableRepository posTableRepository;
  private final PosTableActivityRepository posTableActivityRepository;

  @Override
  public PosTableActivity create(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    PosTableActivity posTableActivity = PosTableActivity.create(posTable);

    return posTableActivityRepository.save(posTableActivity);
  }

}
