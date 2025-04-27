package com.everyonewaiter.application.account.service;

import com.everyonewaiter.application.account.service.request.AccountCreate;
import com.everyonewaiter.application.account.service.request.AccountSignIn;
import com.everyonewaiter.application.account.service.response.AccountAdminRead;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.account.service.AccountValidator;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
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
    accountValidator.validateUnique(request.email(), request.phoneNumber());
    Account account = Account.create(
        request.email(),
        passwordEncoder.encode(request.password()),
        request.phoneNumber()
    );
    return accountRepository.save(account).getId();
  }

  public void checkAvailablePhoneNumber(String phoneNumber) {
    accountValidator.validatePhoneNumberUnique(phoneNumber);
  }

  public void checkPossibleSendAuthMail(String email) {
    accountValidator.validateAccountIsInactive(email);
  }

  @Transactional
  public void activate(String email) {
    Account account = accountRepository.findByEmailOrThrow(email);
    account.activate();
    accountRepository.save(account);
  }

  @Transactional
  public Long signIn(AccountSignIn request) {
    return accountRepository.findByEmail(request.email())
        .map(account -> {
          account.signIn(passwordEncoder, request.password());
          return accountRepository.save(account).getId();
        })
        .orElseThrow(() -> new BusinessException(ErrorCode.FAILED_SIGN_IN));
  }

  public AccountAdminRead.Response readByAdmin(Long accountId) {
    Account account = accountRepository.findByIdOrThrow(accountId);
    return AccountAdminRead.Response.from(account);
  }

}
