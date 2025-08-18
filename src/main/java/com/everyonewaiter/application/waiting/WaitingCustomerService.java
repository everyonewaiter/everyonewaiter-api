package com.everyonewaiter.application.waiting;

import static com.everyonewaiter.domain.waiting.WaitingState.REGISTRATION;

import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.application.waiting.provided.WaitingCustomer;
import com.everyonewaiter.application.waiting.required.WaitingRepository;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.waiting.AlreadyRegisteredWaitingException;
import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingMyTurnView;
import com.everyonewaiter.domain.waiting.WaitingRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
class WaitingCustomerService implements WaitingCustomer {

  private final StoreFinder storeFinder;
  private final WaitingRepository waitingRepository;

  @Override
  @Transactional
  public Waiting register(Long storeId, WaitingRegisterRequest registerRequest) {
    validateWaitingRegister(new PhoneNumber(registerRequest.phoneNumber()));

    Store store = storeFinder.findOrThrow(storeId);

    Waiting waiting = Waiting.register(
        store,
        registerRequest,
        waitingRepository.findLastNumber(storeId),
        waitingRepository.count(storeId, REGISTRATION)
    );

    return waitingRepository.save(waiting);
  }

  private void validateWaitingRegister(PhoneNumber phoneNumber) {
    if (waitingRepository.exists(phoneNumber, REGISTRATION)) {
      throw new AlreadyRegisteredWaitingException();
    }
  }

  @Override
  @Transactional
  public Waiting cancel(Long storeId, String accessKey) {
    Waiting waiting = waitingRepository.findOrThrow(storeId, accessKey);

    waiting.cancel(true);

    return waitingRepository.save(waiting);
  }

  @Override
  @ReadOnlyTransactional
  public WaitingMyTurnView getMyTurn(Long storeId, String accessKey) {
    Waiting waiting = waitingRepository.findOrThrow(storeId, accessKey);

    int currentCount = waitingRepository.countLessThanId(waiting.getId(), storeId, REGISTRATION);

    return WaitingMyTurnView.of(waiting, currentCount);
  }

}
