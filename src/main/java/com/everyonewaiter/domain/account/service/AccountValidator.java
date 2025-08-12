package com.everyonewaiter.domain.account.service;

import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.PhoneNumber;
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
    if (accountRepository.exists(new Email(email))) {
      throw new BusinessException(ErrorCode.ALREADY_USE_EMAIL);
    }
  }

  public void validatePhoneNumberUnique(String phoneNumber) {
    if (accountRepository.exists(new PhoneNumber(phoneNumber))) {
      throw new BusinessException(ErrorCode.ALREADY_USE_PHONE_NUMBER);
    }
  }

}
