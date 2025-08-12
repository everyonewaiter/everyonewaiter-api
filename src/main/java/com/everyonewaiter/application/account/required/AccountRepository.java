package com.everyonewaiter.application.account.required;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.view.AccountAdminView;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.annotation.Nullable;
import java.util.Optional;

public interface AccountRepository {

  boolean exists(Email email);

  boolean existsInactive(Email email);

  boolean exists(PhoneNumber phoneNumber);

  Paging<AccountAdminView.Page> findAllByAdmin(
      @Nullable String email,
      @Nullable Account.State state,
      @Nullable Account.Permission permission,
      @Nullable Boolean hasStore,
      Pagination pagination
  );

  Optional<Account> findById(Long accountId);

  Account findByIdOrThrow(Long accountId);

  Optional<Account> findByEmail(Email email);

  Account findByEmailOrThrow(Email email);

  Account findByPhoneOrThrow(PhoneNumber phoneNumber);

  Account save(Account account);

}
