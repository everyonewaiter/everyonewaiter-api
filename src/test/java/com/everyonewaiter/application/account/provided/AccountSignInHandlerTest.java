package com.everyonewaiter.application.account.provided;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createAccountSignInRequest;
import static com.everyonewaiter.domain.account.AccountFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountSignInRequest;
import com.everyonewaiter.domain.account.AccountState;
import com.everyonewaiter.domain.account.FailedSignInException;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.auth.SignInToken;
import com.everyonewaiter.domain.auth.SignInTokenRenewRequest;
import com.everyonewaiter.domain.shared.AccessDeniedException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@RequiredArgsConstructor
class AccountSignInHandlerTest extends IntegrationTest {

  private final JwtProvider jwtProvider;
  private final AccountRepository accountRepository;
  private final AccountSignInHandler accountSignInHandler;

  @Test
  void signIn() {
    Account account = createAccount();

    SignInToken signInToken = accountSignInHandler.signIn(createAccountSignInRequest());

    JwtPayload payload = jwtProvider.decode(signInToken.accessToken()).orElseThrow();

    assertThat(payload.id()).isEqualTo(account.getId());
  }

  @Test
  void signInFail() {
    assertThatThrownBy(() -> accountSignInHandler.signIn(createAccountSignInRequest()))
        .isInstanceOf(FailedSignInException.class);
  }

  @Test
  void renew() {
    Account account = createAccount();

    var token = accountSignInHandler.signIn(createAccountSignInRequest());

    var renew = accountSignInHandler.renew(new SignInTokenRenewRequest(token.refreshToken()));

    JwtPayload payload = jwtProvider.decode(renew.accessToken()).orElseThrow();

    assertThat(payload.id()).isEqualTo(account.getId());
  }

  @Test
  void renewFail() {
    createAccount();

    var token = accountSignInHandler.signIn(createAccountSignInRequest());

    var request = new SignInTokenRenewRequest(token.refreshToken());

    accountSignInHandler.renew(request);

    assertThatThrownBy(() -> accountSignInHandler.renew(request))
        .isInstanceOf(AccessDeniedException.class);
  }

  private Account createAccount() {
    Account account = Account.create(createAccountCreateRequest(), createPasswordEncoder());

    ReflectionTestUtils.setField(account, "state", AccountState.ACTIVE);

    return accountRepository.save(account);
  }

  @Test
  void accountSignInRequestFail() {
    checkValidation(new AccountSignInRequest("admin@everyonewaiter", "@password1"));
    checkValidation(new AccountSignInRequest("user@gmail.com", "@invalid"));
  }

  private void checkValidation(AccountSignInRequest signInRequest) {
    assertThatThrownBy(() -> accountSignInHandler.signIn(signInRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void signInTokenRenewRequestFail() {
    checkValidation(new SignInTokenRenewRequest(null));
    checkValidation(new SignInTokenRenewRequest(""));
    checkValidation(new SignInTokenRenewRequest("  "));
  }

  private void checkValidation(SignInTokenRenewRequest signInTokenRenewRequest) {
    assertThatThrownBy(() -> accountSignInHandler.renew(signInTokenRenewRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
