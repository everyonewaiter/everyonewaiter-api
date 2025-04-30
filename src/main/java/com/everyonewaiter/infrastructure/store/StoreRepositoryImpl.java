package com.everyonewaiter.infrastructure.store;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class StoreRepositoryImpl implements StoreRepository {

  private final StoreJpaRepository storeJpaRepository;

  @Override
  public Store save(Store store) {
    return storeJpaRepository.save(store);
  }

}
