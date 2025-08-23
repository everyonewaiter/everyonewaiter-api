package com.everyonewaiter.application.store.required;

import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreStatus;
import java.util.List;

public interface StoreRepository {

  boolean exists(Long storeId);

  boolean exists(Long storeId, Long accountId);

  boolean exists(Long storeId, StoreStatus status);

  List<Store> findAll(Long accountId);

  List<Store> findAll(PhoneNumber phoneNumber);

  Store findOrThrow(Long storeId);

  Store findOrThrow(Long storeId, Long accountId);

  Store findOrThrow(Long storeId, PhoneNumber phoneNumber);

  Store save(Store store);

}
