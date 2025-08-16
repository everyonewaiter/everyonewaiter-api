package com.everyonewaiter.application.store.required;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.view.StoreView;
import java.util.List;

public interface StoreRepository {

  boolean existsById(Long storeId);

  boolean existsByIdAndAccountId(Long storeId, Long accountId);

  List<StoreView.Simple> findAllSimpleViewByAccountId(Long accountId);

  Store findByIdOrThrow(Long storeId);

  Store findByIdAndAccountIdOrThrow(Long storeId, Long accountId);

  Store save(Store store);

}
