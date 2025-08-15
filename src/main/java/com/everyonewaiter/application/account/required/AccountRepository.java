package com.everyonewaiter.application.account.required;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminReadRequest;
import com.everyonewaiter.domain.account.AccountAdminView;
import com.everyonewaiter.domain.account.AccountState;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import java.util.Optional;

public interface AccountRepository {

  boolean exists(Email email);

  boolean existsState(Email email, AccountState state);

  boolean exists(PhoneNumber phoneNumber);

  boolean existsState(PhoneNumber phoneNumber, AccountState state);

  Paging<AccountAdminView> findAllByAdmin(AccountAdminReadRequest readRequest);

  Optional<Account> findById(Long accountId);

  Account findByIdOrThrow(Long accountId);

  Optional<Account> findByEmail(Email email);

  Account findByEmailOrThrow(Email email);

  Account findByPhoneOrThrow(PhoneNumber phoneNumber);

  Account save(Account account);

}
