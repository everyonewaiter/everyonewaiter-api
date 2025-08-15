package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminReadRequest;
import com.everyonewaiter.domain.account.AccountAdminView;
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
    return accountRepository.findById(accountId);
  }

  @Override
  public Account findOrThrow(Long accountId) {
    return accountRepository.findByIdOrThrow(accountId);
  }

  @Override
  public Account findOrThrow(PhoneNumber phoneNumber) {
    return accountRepository.findByPhoneOrThrow(phoneNumber);
  }

  @Override
  public Paging<AccountAdminView> findAllByAdmin(AccountAdminReadRequest readRequest) {
    return accountRepository.findAllByAdmin(readRequest);
  }

}
