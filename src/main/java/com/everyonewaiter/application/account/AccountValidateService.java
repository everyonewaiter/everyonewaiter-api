package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.provided.AccountValidator;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.account.AlreadyUseEmailException;
import com.everyonewaiter.domain.account.AlreadyUsePhoneException;
import com.everyonewaiter.domain.account.AlreadyVerifiedEmailException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AccountValidateService implements AccountValidator {

  private final AccountRepository accountRepository;

  @Override
  @ReadOnlyTransactional
  public void checkDuplicateEmail(Email email) {
    if (accountRepository.exists(email)) {
      throw new AlreadyUseEmailException();
    }
  }

  @Override
  @ReadOnlyTransactional
  public void checkDuplicatePhone(PhoneNumber phoneNumber) {
    if (accountRepository.exists(phoneNumber)) {
      throw new AlreadyUsePhoneException();
    }
  }

  @Override
  @ReadOnlyTransactional
  public void checkPossibleSendAuthMail(Email email) {
    if (!accountRepository.existsInactive(email)) {
      throw new AlreadyVerifiedEmailException();
    }
  }

  @Override
  @ReadOnlyTransactional
  public void checkPossibleSendAuthCode(PhoneNumber phoneNumber) {
    if (!accountRepository.exists(phoneNumber)) {
      throw new AccountNotFoundException();
    }
  }

}
