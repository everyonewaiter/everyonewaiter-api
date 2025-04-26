package com.everyonewaiter.application.account.service;

import com.everyonewaiter.application.account.service.request.AccountCreate;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.account.service.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

  private final AccountValidator accountValidator;
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Long create(AccountCreate request) {
    accountValidator.validateUnique(request.getEmail(), request.getPhoneNumber());
    Account account = Account.create(
        request.getEmail(),
        passwordEncoder.encode(request.getPassword()),
        request.getPhoneNumber()
    );
    return accountRepository.save(account).getId();
  }

}
