package com.everyonewaiter.application.waiting;

import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.application.waiting.provided.WaitingValidator;
import com.everyonewaiter.application.waiting.required.WaitingRepository;
import com.everyonewaiter.domain.waiting.ExistIncompleteWaitingException;
import com.everyonewaiter.domain.waiting.WaitingState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class WaitingValidateService implements WaitingValidator {

  private final WaitingRepository waitingRepository;

  @Override
  @ReadOnlyTransactional
  public void checkExistsRegistration(Long storeId) {
    if (waitingRepository.exists(storeId, WaitingState.REGISTRATION)) {
      throw new ExistIncompleteWaitingException();
    }
  }

}
