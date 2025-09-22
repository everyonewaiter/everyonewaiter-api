package com.everyonewaiter.application.account.provided;

import static com.everyonewaiter.domain.account.AccountFixture.createAccountCreateRequest;
import static com.everyonewaiter.domain.auth.JwtFixedId.VERIFICATION_EMAIL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountCreateEvent;
import com.everyonewaiter.domain.account.AccountCreateRequest;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import com.everyonewaiter.domain.account.AlreadyUseEmailException;
import com.everyonewaiter.domain.account.AlreadyUsePhoneException;
import com.everyonewaiter.domain.auth.ExpiredVerificationPhoneException;
import com.everyonewaiter.domain.auth.JwtPayload;
import jakarta.validation.ConstraintViolationException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
class AccountRegisterTest extends IntegrationTest {

  private final RedisTemplate<String, String> redisTemplate;
  private final JwtProvider jwtProvider;
  private final AccountRegister accountRegister;
  private final AccountCreateEventListener accountCreateEventListener;

  @AfterEach
  void cleanEvents() {
    accountCreateEventListener.clear();
  }

  @Test
  void register() {
    Account account = createAccount();

    assertThat(account.getState()).isEqualTo(AccountState.INACTIVE);
    assertThat(account.getPermission()).isEqualTo(AccountPermission.USER);
    assertThat(account.getLastSignIn()).isEqualTo(Instant.ofEpochMilli(0));

    List<AccountCreateEvent> events = accountCreateEventListener.getEvents();
    assertThat(events).hasSize(1);
    assertThat(events.getFirst().email()).isEqualTo(account.getEmail());
  }

  @Test
  void registerFail() {
    assertThatThrownBy(() -> accountRegister.register(createAccountCreateRequest()))
        .isInstanceOf(ExpiredVerificationPhoneException.class);

    createAccount();

    assertThatThrownBy(() -> accountRegister.register(createAccountCreateRequest()))
        .isInstanceOf(AlreadyUseEmailException.class);

    assertThatThrownBy(() -> accountRegister.register(createAccountCreateRequest("user@gmail.com")))
        .isInstanceOf(AlreadyUsePhoneException.class);
  }

  @Test
  void activate() {
    Account account = createAccount();

    JwtPayload payload = new JwtPayload(VERIFICATION_EMAIL_ID, account.getEmail().address());
    String token = jwtProvider.encode(payload, Duration.ofMinutes(10));

    account = accountRegister.activate(token);

    assertThat(account.getState()).isEqualTo(AccountState.ACTIVE);
  }

  private Account createAccount() {
    redisTemplate.opsForValue().set("auth:success:01012345678", "-2");

    return accountRegister.register(createAccountCreateRequest());
  }

  @Test
  void accountCreateRequestFail() {
    checkValidation(new AccountCreateRequest("admin@everyonewaiter", "@password1", "01012345678"));
    checkValidation(new AccountCreateRequest("user@gmail.com", "@invalid", "01012345678"));
    checkValidation(new AccountCreateRequest("user@gmail.com", "@password1", "0551234567"));
  }

  private void checkValidation(AccountCreateRequest createRequest) {
    assertThatThrownBy(() -> accountRegister.register(createRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
