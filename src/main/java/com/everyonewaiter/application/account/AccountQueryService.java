package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminPageView;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class AccountQueryService implements AccountFinder {

  private final AccountRepository accountRepository;

  @Override
  public Optional<Account> find(Long accountId) {
    return accountRepository.find(accountId);
  }

  @Override
  public Optional<Account> find(Email email) {
    return accountRepository.find(email);
  }

  @Override
  public Account findOrThrow(Long accountId) {
    return accountRepository.findOrThrow(accountId);
  }

  @Override
  public Account findOrThrow(Email email) {
    return accountRepository.findOrThrow(email);
  }

  @Override
  public Account findOrThrow(PhoneNumber phoneNumber) {
    return accountRepository.findOrThrow(phoneNumber);
  }

  @Override
  public Paging<AccountAdminPageView> findAllByAdmin(AccountAdminPageRequest pageRequest) {
    return accountRepository.findAll(pageRequest);
  }

}
