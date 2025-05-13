package com.everyonewaiter.application.waiting;

import com.everyonewaiter.application.waiting.request.WaitingWrite;
import com.everyonewaiter.application.waiting.response.WaitingResponse;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import com.everyonewaiter.domain.waiting.service.WaitingValidator;
import java.util.List;
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
    int lastWaitingNumber = waitingRepository.countByStoreId(storeId);
    int currentWaitingCount = waitingRepository.countByStoreIdAndState(
        storeId,
        Waiting.State.REGISTRATION
    );
    Waiting waiting = Waiting.create(
        store,
        request.phoneNumber(),
        request.adult(),
        request.infant(),
        lastWaitingNumber,
        currentWaitingCount
    );

    return waitingRepository.save(waiting).getId();
  }

  public WaitingResponse.RegistrationCount getRegistrationCount(Long storeId) {
    int count = waitingRepository.countByStoreIdAndState(storeId, Waiting.State.REGISTRATION);
    return WaitingResponse.RegistrationCount.from(count);
  }

  public WaitingResponse.Details readAll(Long storeId) {
    List<Waiting> waitings = waitingRepository.findAllByStoreIdAndState(
        storeId,
        Waiting.State.REGISTRATION
    );
    return WaitingResponse.Details.from(waitings);
  }

}
