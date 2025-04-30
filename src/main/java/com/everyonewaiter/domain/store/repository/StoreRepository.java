package com.everyonewaiter.domain.store.repository;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.view.StoreView;
import java.util.List;
import java.util.Optional;

public interface StoreRepository {

  List<StoreView.Simple> findAllSimpleViewByAccountId(Long accountId);

  Optional<Store> findByIdAndAccountId(Long storeId, Long accountId);

  Store findByIdAndAccountIdOrThrow(Long storeId, Long accountId);

  Store save(Store store);

}
