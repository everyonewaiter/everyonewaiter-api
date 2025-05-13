package com.everyonewaiter.domain.waiting.repository;

import com.everyonewaiter.domain.waiting.entity.Waiting;
import java.util.List;

public interface WaitingRepository {

  int countByStoreId(Long storeId);

  int countByStoreIdAndState(Long storeId, Waiting.State state);

  boolean existsByPhoneNumberAndState(String phoneNumber, Waiting.State state);

  List<Waiting> findAllByStoreIdAndState(Long storeId, Waiting.State state);

  Waiting findByIdAndStoreIdOrThrow(Long waitingId, Long storeId);

  Waiting findByStoreIdAndAccessKey(Long storeId, String accessKey);

  Waiting save(Waiting waiting);

}
