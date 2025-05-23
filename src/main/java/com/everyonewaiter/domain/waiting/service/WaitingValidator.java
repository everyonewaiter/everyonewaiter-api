package com.everyonewaiter.domain.waiting.service;

import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
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
