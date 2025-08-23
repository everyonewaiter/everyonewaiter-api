package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.application.store.provided.StoreManager;
import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class StoreModifyService implements StoreManager {

  private final StoreFinder storeFinder;
  private final StoreValidateService storeValidator;
  private final StoreRepository storeRepository;

  @Override
  public Store open(Long storeId) {
    Store store = storeFinder.findOrThrow(storeId);

    store.open();

    return storeRepository.save(store);
  }

  @Override
  public Store close(Long storeId) {
    storeValidator.checkPossibleClose(storeId);

    Store store = storeFinder.findOrThrow(storeId);

    store.close();

    return storeRepository.save(store);
  }

  @Override
  public Store update(Long storeId, Long accountId, StoreUpdateRequest updateRequest) {
    Store store = storeFinder.findOrThrow(storeId, accountId);

    store.update(updateRequest);

    return storeRepository.save(store);
  }

}
