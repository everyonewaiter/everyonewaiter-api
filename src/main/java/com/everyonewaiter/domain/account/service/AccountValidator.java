package com.everyonewaiter.domain.account.service;

import com.everyonewaiter.domain.account.entity.Account;
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
    validateEmailUnique(email);
    validatePhoneNumberUnique(phoneNumber);
  }

  public void validateEmailUnique(String email) {
    if (accountRepository.existsByEmail(email)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_EMAIL);
    }
  }

  public void validatePhoneNumberUnique(String phoneNumber) {
    if (accountRepository.existsByPhoneNumber(phoneNumber)) {
      throw new BusinessException(ErrorCode.ALREADY_USE_PHONE_NUMBER);
    }
  }

  public void validateAccountIsInactive(String email) {
    Account account = getAccount(email);
    if (!account.isInactive()) {
      throw new BusinessException(ErrorCode.ALREADY_VERIFIED_EMAIL);
    }
  }

  private Account getAccount(String email) {
    return accountRepository.findByEmailOrThrow(email);
  }

}
