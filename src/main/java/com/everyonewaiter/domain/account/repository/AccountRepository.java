package com.everyonewaiter.domain.account.repository;

import com.everyonewaiter.domain.account.entity.Account;
import java.util.Optional;

public interface AccountRepository {

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

  Optional<Account> findByEmail(String email);

  Account findByEmailOrThrow(String email);

  Optional<Account> findById(Long accountId);

  Account findByIdOrThrow(Long accountId);

  Account save(Account account);

}
