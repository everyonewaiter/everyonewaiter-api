package com.everyonewaiter.application.account.provided;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminView;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.Valid;
import java.util.Optional;

public interface AccountFinder {

  Optional<Account> find(Long accountId);

  Account findOrThrow(Long accountId);

  Account findOrThrow(PhoneNumber phoneNumber);

  Paging<AccountAdminView> findAllByAdmin(@Valid AccountAdminPageRequest pageRequest);

}
