package com.everyonewaiter.domain.account.service;

import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {

  private final AccountRepository accountRepository;

  public void validateUnique(String email, String phoneNumber) {
    if (accountRepository.existsByEmail(email)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_EMAIL);
    }

    if (accountRepository.existsByPhoneNumber(phoneNumber)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_PHONE_NUMBER);
    }
  }

}
