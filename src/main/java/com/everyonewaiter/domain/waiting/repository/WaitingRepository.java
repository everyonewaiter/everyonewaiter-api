package com.everyonewaiter.domain.waiting.repository;

import com.everyonewaiter.domain.waiting.entity.Waiting;

public interface WaitingRepository {

  int countByStoreIdAndAfterLastOpenedAt(Long storeId);

  int countByStoreIdAndStateAndAfterLastOpenedAt(Long storeId, Waiting.State state);

  boolean existsByPhoneNumberAndState(String phoneNumber, Waiting.State state);

  Waiting save(Waiting waiting);

}
