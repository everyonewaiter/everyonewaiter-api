package com.everyonewaiter.application.account;

import com.everyonewaiter.application.account.provided.AccountFinder;
import com.everyonewaiter.application.account.provided.AccountSignInHandler;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountSignInRequest;
import com.everyonewaiter.domain.account.FailedSignInException;
import com.everyonewaiter.domain.account.PasswordEncoder;
import com.everyonewaiter.domain.account.SignInToken;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.shared.Email;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class AccountSignInService implements AccountSignInHandler {

  private final AccountFinder accountFinder;
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Override
  public SignInToken signIn(AccountSignInRequest signInRequest) {
    Email email = new Email(signInRequest.email());

    Account account = accountFinder.find(email).orElseThrow(FailedSignInException::new);

    account.signIn(signInRequest, passwordEncoder);

    accountRepository.save(account);

    JwtPayload payload = new JwtPayload(account.getId(), account.getEmail().address());
    String accessToken = jwtProvider.encode(payload, Duration.ofHours(3));

    return new SignInToken(accessToken);
  }

}
