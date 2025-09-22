package com.everyonewaiter.application.store.provided;

import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreUpdateRequest;
import jakarta.validation.Valid;

public interface StoreManager {

  Store open(Long storeId);

  Store close(Long storeId);

  Store update(Long storeId, Long accountId, @Valid StoreUpdateRequest updateRequest);

}
