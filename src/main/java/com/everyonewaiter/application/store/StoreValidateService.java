package com.everyonewaiter.application.store;

import com.everyonewaiter.application.pos.provided.PosTableValidator;
import com.everyonewaiter.application.store.provided.StoreValidator;
import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.application.waiting.provided.WaitingValidator;
import com.everyonewaiter.domain.store.ClosedStoreException;
import com.everyonewaiter.domain.store.StoreNotFoundException;
import com.everyonewaiter.domain.store.StoreStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class StoreValidateService implements StoreValidator {

  private final StoreRepository storeRepository;
  private final WaitingValidator waitingValidator;
  private final PosTableValidator posTableValidator;

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
    if (storeRepository.exists(storeId, StoreStatus.CLOSE)) {
      throw new ClosedStoreException();
    }
  }

  @Override
  public void checkPossibleClose(Long storeId) {
    waitingValidator.checkExistsRegistration(storeId);
    posTableValidator.checkExistsActiveActivity(storeId);
  }

}
