package com.everyonewaiter.domain.store.repository;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.view.StoreView;
import java.util.List;

public interface StoreRepository {

  List<StoreView.Simple> findAllSimpleViewByAccountId(Long accountId);

  Store save(Store store);

}
