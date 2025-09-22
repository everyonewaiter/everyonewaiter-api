package com.everyonewaiter.application.waiting.provided;

import com.everyonewaiter.domain.waiting.Waiting;
import java.util.List;

public interface WaitingAdministrator {

  Waiting customerCall(Long waitingId, Long storeId);

  Waiting complete(Long waitingId, Long storeId);

  Waiting cancel(Long waitingId, Long storeId);

  int getCount(Long storeId);

  List<Waiting> findAll(Long storeId);

}
