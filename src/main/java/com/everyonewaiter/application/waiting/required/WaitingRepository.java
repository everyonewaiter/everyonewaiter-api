package com.everyonewaiter.application.waiting.required;

import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingState;
import java.util.List;

public interface WaitingRepository {

  int findLastNumber(Long storeId);

  int count(Long storeId, WaitingState state);

  int countLessThanId(Long waitingId, Long storeId, WaitingState state);

  boolean exists(PhoneNumber phoneNumber, WaitingState state);

  boolean exists(Long storeId, WaitingState state);

  List<Waiting> findAll(Long storeId, WaitingState state);

  Waiting findOrThrow(Long waitingId, Long storeId);

  Waiting findOrThrow(Long storeId, String accessKey);

  Waiting save(Waiting waiting);

}
