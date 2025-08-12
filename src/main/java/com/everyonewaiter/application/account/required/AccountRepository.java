package com.everyonewaiter.application.account.required;

import com.everyonewaiter.application.account.dto.AccountAdminPageView;
import com.everyonewaiter.application.account.dto.AccountAdminReadRequest;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import java.util.Optional;

public interface AccountRepository {

  boolean exists(Email email);

  boolean existsInactive(Email email);

  boolean exists(PhoneNumber phoneNumber);

  Paging<AccountAdminPageView> findAllByAdmin(AccountAdminReadRequest readRequest);

  Optional<Account> findById(Long accountId);

  Account findByIdOrThrow(Long accountId);

  Optional<Account> findByEmail(Email email);

  Account findByEmailOrThrow(Email email);

  Account findByPhoneOrThrow(PhoneNumber phoneNumber);

  Account save(Account account);

}
