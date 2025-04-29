package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.request.AccountAdminPage;
import com.everyonewaiter.application.account.request.AccountAdminUpdate;
import com.everyonewaiter.application.account.request.AccountCreate;
import com.everyonewaiter.application.account.request.AccountSignIn;
import com.everyonewaiter.application.account.response.AccountAdmin;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.account.service.AccountValidator;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.support.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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

  public Paging<AccountAdmin.PageViewResponse> readAllByAdmin(AccountAdminPage request) {
    return accountRepository.findAllByAdmin(
            request.email(),
            request.state(),
            request.permission(),
            request.pagination()
        )
        .map(AccountAdmin.PageViewResponse::from);
  }

  public AccountAdmin.ReadResponse readByAdmin(Long accountId) {
    Account account = accountRepository.findByIdOrThrow(accountId);
    return AccountAdmin.ReadResponse.from(account);
  }

  @Transactional
  public void updateByAdmin(Long accountId, AccountAdminUpdate request) {
    Account account = accountRepository.findByIdOrThrow(accountId);
    account.update(request.state(), request.permission());
    accountRepository.save(account);
  }

}
