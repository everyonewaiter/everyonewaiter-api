package com.everyonewaiter.application.waiting;

import com.everyonewaiter.application.waiting.request.WaitingWrite;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import com.everyonewaiter.domain.waiting.service.WaitingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WaitingService {

  private final StoreRepository storeRepository;
  private final WaitingValidator waitingValidator;
  private final WaitingRepository waitingRepository;

  @Transactional
  public Long create(Long storeId, WaitingWrite.Create request) {
    waitingValidator.validateRegistration(storeId, request.phoneNumber());

    Store store = storeRepository.findByIdOrThrow(storeId);
    int lastWaitingNumber = waitingRepository.countByStoreIdAndAfterLastOpenedAt(storeId);
    int initWaitingTeamCount = waitingRepository.countByStoreIdAndStateAndAfterLastOpenedAt(
        storeId,
        Waiting.State.REGISTRATION
    );
    Waiting waiting = Waiting.create(
        store,
        request.phoneNumber(),
        request.adult(),
        request.infant(),
        lastWaitingNumber,
        initWaitingTeamCount
    );

    return waitingRepository.save(waiting).getId();
  }

}
