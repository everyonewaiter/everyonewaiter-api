package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.provided.AccountValidator;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.account.AccountState;
import com.everyonewaiter.domain.account.AlreadyUseEmailException;
import com.everyonewaiter.domain.account.AlreadyUsePhoneException;
import com.everyonewaiter.domain.account.AlreadyVerifiedEmailException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class AccountValidateService implements AccountValidator {

  private final AccountRepository accountRepository;

  @Override
  public void checkDuplicateEmail(Email email) {
    if (accountRepository.exists(email)) {
      throw new AlreadyUseEmailException();
    }
  }

  @Override
  public void checkDuplicatePhone(PhoneNumber phoneNumber) {
    if (accountRepository.exists(phoneNumber)) {
      throw new AlreadyUsePhoneException();
    }
  }

  @Override
  public void checkPossibleSendAuthMail(Email email) {
    if (!accountRepository.exists(email, AccountState.INACTIVE)) {
      throw new AlreadyVerifiedEmailException();
    }
  }

  @Override
  public void checkPossibleSendAuthCode(PhoneNumber phoneNumber) {
    if (!accountRepository.exists(phoneNumber, AccountState.ACTIVE)) {
      throw new AccountNotFoundException();
    }
  }

}
