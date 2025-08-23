package com.everyonewaiter.application.store;

import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.application.store.provided.StoreValidator;
import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.application.waiting.required.WaitingRepository;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.store.ClosedStoreException;
import com.everyonewaiter.domain.store.StoreNotFoundException;
import com.everyonewaiter.domain.store.StoreStatus;
import com.everyonewaiter.domain.waiting.WaitingState;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class StoreValidateService implements StoreValidator {

  private final StoreRepository storeRepository;
  private final PosTableRepository posTableRepository;
  private final WaitingRepository waitingRepository;

  @Override
  public void checkExists(Long storeId) {
    if (!storeRepository.exists(storeId)) {
      throw new StoreNotFoundException();
    }
  }

  @Override
  public void checkExists(Long storeId, Long accountId) {
    if (!storeRepository.exists(storeId, accountId)) {
      throw new StoreNotFoundException();
    }
  }

  @Override
  public void checkIsOpened(Long storeId) {
    if (storeRepository.existsStatus(storeId, StoreStatus.CLOSE)) {
      throw new ClosedStoreException();
    }
  }

  @Override
  public void checkPossibleClose(Long storeId) {
    List<PosTable> posTables = posTableRepository.findAllActive(storeId);

    if (posTables.stream().anyMatch(PosTable::hasActiveActivity)) {
      throw new BusinessException(ErrorCode.INCOMPLETE_POS_TABLE_ACTIVITY);
    }
    if (waitingRepository.exists(storeId, WaitingState.REGISTRATION)) {
      throw new BusinessException(ErrorCode.INCOMPLETE_WAITING);
    }
  }

}
