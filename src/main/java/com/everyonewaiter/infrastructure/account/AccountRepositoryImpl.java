package com.everyonewaiter.infrastructure.account;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AccountRepositoryImpl implements AccountRepository {

  private final AccountJpaRepository accountJpaRepository;

  @Override
  public boolean existsByEmail(String email) {
    return accountJpaRepository.existsByEmail(email);
  }

  @Override
  public boolean existsByPhoneNumber(String phoneNumber) {
    return accountJpaRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public Account save(Account account) {
    return accountJpaRepository.save(account);
  }

}
