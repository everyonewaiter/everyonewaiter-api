package com.everyonewaiter.domain.waiting.service;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingValidator {

  private final WaitingRepository waitingRepository;

  public void validateRegistration(String phoneNumber) {
    if (waitingRepository.existsByPhoneNumberAndState(phoneNumber, Waiting.State.REGISTRATION)) {
      throw new BusinessException(ErrorCode.ALREADY_REGISTERED_WAITING);
    }
  }

}
