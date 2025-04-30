package com.everyonewaiter.domain.store.repository;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.view.StoreSimpleView;
import java.util.List;

public interface StoreRepository {

  List<StoreSimpleView> findAllSimpleViewByAccountId(Long accountId);

  Store save(Store store);

}
