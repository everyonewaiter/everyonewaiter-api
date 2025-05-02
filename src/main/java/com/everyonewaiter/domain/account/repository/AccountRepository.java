package com.everyonewaiter.domain.account.repository;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.view.AccountAdminView;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;
import jakarta.annotation.Nullable;
import java.util.Optional;

public interface AccountRepository {

  boolean existsByEmail(String email);

  boolean existsByPhone(String phoneNumber);

  Paging<AccountAdminView.Page> findAllByAdmin(
      @Nullable String email,
      @Nullable Account.State state,
      @Nullable Account.Permission permission,
      @Nullable Boolean hasStore,
      Pagination pagination
  );

  Optional<Account> findById(Long accountId);

  Account findByIdOrThrow(Long accountId);

  Optional<Account> findByEmail(String email);

  Account findByEmailOrThrow(String email);

  Account findByPhoneOrThrow(String phoneNumber);

  Account save(Account account);

}
