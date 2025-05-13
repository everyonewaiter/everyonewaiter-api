package com.everyonewaiter.domain.waiting.service;

import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingValidator {

  private final StoreRepository storeRepository;
  private final WaitingRepository waitingRepository;

  public void validateRegistration(Long storeId, String phoneNumber) {
    if (storeRepository.findByIdOrThrow(storeId).isClosed()) {
      throw new BusinessException(ErrorCode.STORE_IS_CLOSED);
    }

    if (waitingRepository.existsByPhoneNumberAndState(phoneNumber, Waiting.State.REGISTRATION)) {
      throw new BusinessException(ErrorCode.ALREADY_REGISTERED_WAITING);
    }
  }

}
