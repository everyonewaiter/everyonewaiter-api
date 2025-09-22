package com.everyonewaiter.application.account.required;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminPageView;
import com.everyonewaiter.domain.account.AccountState;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import java.util.Optional;

public interface AccountRepository {

  boolean exists(Email email);

  boolean exists(Email email, AccountState state);

  boolean exists(PhoneNumber phoneNumber);

  boolean exists(PhoneNumber phoneNumber, AccountState state);

  Paging<AccountAdminPageView> findAll(AccountAdminPageRequest pageRequest);

  Optional<Account> find(Long accountId);

  Account findOrThrow(Long accountId);

  Optional<Account> find(Email email);

  Account findOrThrow(Email email);

  Account findOrThrow(PhoneNumber phoneNumber);

  Account save(Account account);

}
