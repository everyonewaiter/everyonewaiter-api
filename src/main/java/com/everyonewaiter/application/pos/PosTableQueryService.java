package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.provided.PosTableFinder;
import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosView;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class PosTableQueryService implements PosTableFinder {

  private final PosTableRepository posTableRepository;

  @Override
  @Transactional(readOnly = true)
  public List<PosView.PosTableDetail> findAllActive(Long storeId) {
    return posTableRepository.findAllActive(storeId)
        .stream()
        .map(PosView.PosTableDetail::from)
        .toList();
  }

  @Override
  @ReadOnlyTransactional
  public PosTable findActiveOrThrow(Long storeId, int tableNo) {
    return posTableRepository.findActiveOrThrow(storeId, tableNo);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PosView.PosTableActivityDetail> findActiveActivity(Long storeId, int tableNo) {
    return posTableRepository.findActiveOrThrow(storeId, tableNo)
        .getActiveActivity()
        .map(PosView.PosTableActivityDetail::from);
  }

}
