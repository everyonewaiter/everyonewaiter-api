package com.everyonewaiter.application.waiting;

import static com.everyonewaiter.domain.waiting.WaitingState.REGISTRATION;

import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.application.waiting.provided.WaitingAdministrator;
import com.everyonewaiter.application.waiting.required.WaitingRepository;
import com.everyonewaiter.domain.waiting.Waiting;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
class WaitingAdminService implements WaitingAdministrator {

  private final WaitingRepository waitingRepository;

  @Override
  @Transactional
  @DistributedLock(key = "#storeId + '-waiting'")
  public Waiting customerCall(Long waitingId, Long storeId) {
    Waiting waiting = waitingRepository.findOrThrow(waitingId, storeId);

    waiting.customerCall();

    return waitingRepository.save(waiting);
  }

  @Override
  @Transactional
  @DistributedLock(key = "#storeId + '-waiting'")
  public Waiting complete(Long waitingId, Long storeId) {
    Waiting waiting = waitingRepository.findOrThrow(waitingId, storeId);

    waiting.complete();

    return waitingRepository.save(waiting);
  }

  @Override
  @Transactional
  @DistributedLock(key = "#storeId + '-waiting'")
  public Waiting cancel(Long waitingId, Long storeId) {
    Waiting waiting = waitingRepository.findOrThrow(waitingId, storeId);

    waiting.cancel(false);

    return waitingRepository.save(waiting);
  }

  @Override
  @ReadOnlyTransactional
  public int getCount(Long storeId) {
    return waitingRepository.count(storeId, REGISTRATION);
  }

  @Override
  @ReadOnlyTransactional
  public List<Waiting> findAll(Long storeId) {
    return waitingRepository.findAll(storeId, REGISTRATION);
  }

}
