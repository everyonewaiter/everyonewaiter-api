package com.everyonewaiter.application.store.required;

import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreStatus;
import java.util.List;

public interface StoreRepository {

  boolean exists(Long storeId);

  boolean exists(Long storeId, Long accountId);

  boolean existsStatus(Long storeId, StoreStatus status);

  List<Store> findAll(Long accountId);

  List<Store> findAll(PhoneNumber phoneNumber);

  Store findByIdOrThrow(Long storeId);

  Store findByIdAndAccountIdOrThrow(Long storeId, Long accountId);

  Store findByIdAndPhoneOrThrow(Long storeId, PhoneNumber phoneNumber);

  Store save(Store store);

}
