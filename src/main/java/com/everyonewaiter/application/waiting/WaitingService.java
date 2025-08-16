package com.everyonewaiter.application.waiting;

import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.application.waiting.request.WaitingWrite;
import com.everyonewaiter.application.waiting.response.WaitingResponse;
import com.everyonewaiter.domain.store.entity.Store;
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
    waitingValidator.validateRegistration(request.phoneNumber());

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

  @Transactional
  public void call(Long waitingId, Long storeId) {
    Waiting waiting = waitingRepository.findByIdAndStoreIdOrThrow(waitingId, storeId);
    waiting.call();
    waitingRepository.save(waiting);
  }

  @Transactional
  public void complete(Long waitingId, Long storeId) {
    Waiting waiting = waitingRepository.findByIdAndStoreIdOrThrow(waitingId, storeId);
    waiting.complete();
    waitingRepository.save(waiting);
  }

  @Transactional
  public void cancel(Long waitingId, Long storeId) {
    Waiting waiting = waitingRepository.findByIdAndStoreIdOrThrow(waitingId, storeId);
    waiting.cancelByStore();
    waitingRepository.save(waiting);
  }

  @Transactional
  public void cancel(Long storeId, String accessKey) {
    Waiting waiting = waitingRepository.findByStoreIdAndAccessKey(storeId, accessKey);
    waiting.cancelByCustomer();
    waitingRepository.save(waiting);
  }

  public WaitingResponse.RegistrationCount getRegistrationCount(Long storeId) {
    int count = waitingRepository.countByStoreIdAndState(storeId, Waiting.State.REGISTRATION);
    return WaitingResponse.RegistrationCount.from(count);
  }

  public WaitingResponse.MyTurn getMyTurn(Long storeId, String accessKey) {
    Waiting waiting = waitingRepository.findByStoreIdAndAccessKey(storeId, accessKey);
    int currentWaitingTeamCount = waitingRepository.countByIdAndStoreIdAndState(
        waiting.getId(),
        storeId,
        Waiting.State.REGISTRATION
    );
    return WaitingResponse.MyTurn.of(waiting, currentWaitingTeamCount);
  }

  public WaitingResponse.Details readAll(Long storeId) {
    List<Waiting> waitings = waitingRepository.findAllByStoreIdAndState(
        storeId,
        Waiting.State.REGISTRATION
    );
    return WaitingResponse.Details.from(waitings);
  }

}
