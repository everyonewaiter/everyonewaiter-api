package com.everyonewaiter.application.store.provided;

import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.Store;
import java.util.List;

public interface StoreFinder {

  List<Store> findAll(Long accountId);

  List<Store> findAll(PhoneNumber phoneNumber);

  Store findOrThrow(Long storeId);

  Store findOrThrow(Long storeId, PhoneNumber phoneNumber);

  Store findOrThrow(Long storeId, Long accountId);

}
