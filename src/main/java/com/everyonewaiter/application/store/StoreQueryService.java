package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.Store;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class StoreQueryService implements StoreFinder {

  private final StoreRepository storeRepository;

  @Override
  public List<Store> findAll(Long accountId) {
    return storeRepository.findAll(accountId);
  }

  @Override
  public List<Store> findAll(PhoneNumber phoneNumber) {
    return storeRepository.findAll(phoneNumber);
  }

  @Override
  public Store findOrThrow(Long storeId) {
    return storeRepository.findOrThrow(storeId);
  }

  @Override
  public Store findOrThrow(Long storeId, PhoneNumber phoneNumber) {
    return storeRepository.findOrThrow(storeId, phoneNumber);
  }

  @Override
  public Store findOrThrow(Long storeId, Long accountId) {
    return storeRepository.findOrThrow(storeId, accountId);
  }

}
