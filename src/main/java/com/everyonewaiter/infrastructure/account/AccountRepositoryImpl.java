package com.everyonewaiter.infrastructure.account;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.util.Optional;
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
  public Optional<Account> findByEmail(String email) {
    return accountJpaRepository.findByEmail(email);
  }

  @Override
  public Account findByEmailOrThrow(String email) {
    return findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
  }

  @Override
  public Optional<Account> findById(Long id) {
    return accountJpaRepository.findById(id);
  }

  @Override
  public Account save(Account account) {
    return accountJpaRepository.save(account);
  }

}
