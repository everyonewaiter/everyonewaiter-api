package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.request.AccountAdminRead;
import com.everyonewaiter.application.account.request.AccountAdminWrite;
import com.everyonewaiter.application.account.request.AccountWrite;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.account.response.AccountAdminResponse;
import com.everyonewaiter.application.account.response.AccountResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.service.AccountValidator;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
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
  public Long create(AccountWrite.Create request) {
    accountValidator.validateUnique(request.email(), request.phoneNumber());
    Account account = Account.create(
        request.email(),
        passwordEncoder.encode(request.password()),
        request.phoneNumber()
    );
    return accountRepository.save(account).getId();
  }

  @Transactional
  public void activate(String email) {
    Account account = accountRepository.findByEmailOrThrow(new Email(email));
    account.activate();
    accountRepository.save(account);
  }

  @Transactional
  public Long signIn(AccountWrite.SignIn request) {
    return accountRepository.findByEmail(new Email(request.email()))
        .map(account -> {
          account.signIn(passwordEncoder, request.password());
          return accountRepository.save(account).getId();
        })
        .orElseThrow(() -> new BusinessException(ErrorCode.FAILED_SIGN_IN));
  }

  public Long getAccountIdByPhone(String phoneNumber) {
    return accountRepository.findByPhoneOrThrow(new PhoneNumber(phoneNumber)).getId();
  }

  public Paging<AccountAdminResponse.PageView> readAllByAdmin(AccountAdminRead.PageView request) {
    return accountRepository.findAllByAdmin(
            request.email(),
            request.state(),
            request.permission(),
            request.hasStore(),
            request.pagination()
        )
        .map(AccountAdminResponse.PageView::from);
  }

  public AccountAdminResponse.Detail readByAdmin(Long accountId) {
    Account account = accountRepository.findByIdOrThrow(accountId);
    return AccountAdminResponse.Detail.from(account);
  }

  public AccountResponse.Profile readByPhone(String phoneNumber) {
    Account account = accountRepository.findByPhoneOrThrow(new PhoneNumber(phoneNumber));
    return AccountResponse.Profile.from(account);
  }

  @Transactional
  public void updateByAdmin(Long accountId, AccountAdminWrite.Update request) {
    Account account = accountRepository.findByIdOrThrow(accountId);
    account.update(request.state(), request.permission());
    accountRepository.save(account);
  }

}
