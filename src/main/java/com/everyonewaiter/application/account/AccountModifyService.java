package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.account.provided.AccountRegister;
import com.everyonewaiter.application.account.provided.AccountUpdater;
import com.everyonewaiter.application.account.provided.AccountValidator;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.auth.provided.Authenticator;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminUpdateRequest;
import com.everyonewaiter.domain.account.AccountCreateRequest;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.PasswordEncoder;
import com.everyonewaiter.domain.auth.AuthPurpose;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class AccountModifyService implements AccountRegister, AccountUpdater {

  private final Authenticator authenticator;
  private final AccountFinder accountFinder;
  private final AccountValidator accountValidator;
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Account register(AccountCreateRequest createRequest) {
    validateAccountCreate(createRequest);

    Account account = Account.create(createRequest, passwordEncoder);

    return accountRepository.save(account);
  }

  private void validateAccountCreate(AccountCreateRequest createRequest) {
    Email email = new Email(createRequest.email());
    PhoneNumber phoneNumber = new PhoneNumber(createRequest.phoneNumber());

    authenticator.checkAuthSuccess(AuthPurpose.SIGN_UP, phoneNumber);
    accountValidator.checkDuplicateEmail(email);
    accountValidator.checkDuplicatePhone(phoneNumber);
  }

  @Override
  public Account activate(String authMailToken) {
    Email email = authenticator.verifyAuthMail(authMailToken);

    Account account = accountFinder.findOrThrow(email);

    account.activate();

    return accountRepository.save(account);
  }

  @Override
  public Account authorize(Long accountId, AccountPermission permission) {
    Account account = accountFinder.findOrThrow(accountId);

    account.authorize(permission);

    return accountRepository.save(account);
  }

  @Override
  public Account updateByAdmin(
      Account adminAccount,
      Long userAccountId,
      AccountAdminUpdateRequest updateRequest
  ) {
    Account userAccount = accountFinder.findOrThrow(userAccountId);

    adminAccount.update(userAccount, updateRequest);

    return accountRepository.save(userAccount);
  }

}
