package com.everyonewaiter.domain.account.repository;

import com.everyonewaiter.domain.account.entity.Account;

public interface AccountRepository {

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

  Account save(Account account);

}
