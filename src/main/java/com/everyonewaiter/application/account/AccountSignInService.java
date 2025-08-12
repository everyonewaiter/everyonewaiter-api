package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.provided.AccountSignInHandler;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.auth.dto.SignInTokenRenewRequest;
import com.everyonewaiter.application.auth.dto.TokenResponse;
import com.everyonewaiter.application.auth.provided.SignInTokenProvider;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountSignInRequest;
import com.everyonewaiter.domain.account.FailedSignInException;
import com.everyonewaiter.domain.account.PasswordEncoder;
import com.everyonewaiter.domain.shared.AccessDeniedException;
import com.everyonewaiter.domain.shared.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class AccountSignInService implements AccountSignInHandler {

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final SignInTokenProvider signInTokenProvider;

  @Override
  public TokenResponse signIn(AccountSignInRequest signInRequest) {
    Email email = new Email(signInRequest.email());

    Account account = accountRepository.findByEmail(email).orElseThrow(FailedSignInException::new);

    account.signIn(signInRequest, passwordEncoder);

    accountRepository.save(account);

    return signInTokenProvider.createToken(account.getId());
  }

  @Override
  public TokenResponse renew(SignInTokenRenewRequest signInTokenRenewRequest) {
    return signInTokenProvider.renewToken(signInTokenRenewRequest)
        .orElseThrow(AccessDeniedException::new);
  }

}
