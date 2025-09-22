package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.provided.PosTableValidator;
import com.everyonewaiter.application.pos.required.PosTableActivityRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.pos.ExistIncompletePosTableActivityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class PosTableValidateService implements PosTableValidator {

  private final PosTableActivityRepository posTableActivityRepository;

  @Override
  @ReadOnlyTransactional
  public void checkExistsActiveActivity(Long storeId) {
    if (posTableActivityRepository.existsActive(storeId)) {
      throw new ExistIncompletePosTableActivityException();
    }
  }

}
