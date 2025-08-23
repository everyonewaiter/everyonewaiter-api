package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminPageView;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.Valid;
import java.util.Optional;

public interface AccountFinder {

  Optional<Account> find(Long accountId);

  Optional<Account> find(Email email);

  Account findOrThrow(Long accountId);

  Account findOrThrow(Email email);

  Account findOrThrow(PhoneNumber phoneNumber);

  Paging<AccountAdminPageView> findAllByAdmin(@Valid AccountAdminPageRequest pageRequest);

}
