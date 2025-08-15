package com.everyonewaiter.application.auth.provided;

import static com.everyonewaiter.domain.account.AccountState.ACTIVE;
import static com.everyonewaiter.domain.account.AccountState.INACTIVE;
import static com.everyonewaiter.domain.auth.AuthFixture.createSendAuthCodeRequest;
import static com.everyonewaiter.domain.auth.AuthFixture.createSendAuthMailRequest;
import static com.everyonewaiter.domain.auth.AuthFixture.createVerifyAuthCodeRequest;
import static com.everyonewaiter.domain.auth.AuthPurpose.CREATE_DEVICE;
import static com.everyonewaiter.domain.auth.AuthPurpose.SIGN_UP;
import static com.everyonewaiter.domain.auth.JwtFixedId.VERIFICATION_EMAIL_ID;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.auth.required.JwtProvider;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.account.AlreadyUsePhoneException;
import com.everyonewaiter.domain.account.AlreadyVerifiedEmailException;
import com.everyonewaiter.domain.auth.AlreadyVerifiedPhoneException;
import com.everyonewaiter.domain.auth.AuthCodeSendEvent;
import com.everyonewaiter.domain.auth.AuthMailSendEvent;
import com.everyonewaiter.domain.auth.ExceedMaximumVerificationException;
import com.everyonewaiter.domain.auth.ExpiredVerificationEmailException;
import com.everyonewaiter.domain.auth.ExpiredVerificationPhoneException;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.auth.SendAuthCodeRequest;
import com.everyonewaiter.domain.auth.SendAuthMailRequest;
import com.everyonewaiter.domain.auth.VerifyAuthCodeRequest;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.PhoneNumber;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@RequiredArgsConstructor
class AuthenticatorTest extends IntegrationTest {

  @MockitoSpyBean
  private AccountRepository accountRepository;

  @MockitoSpyBean
  private JwtProvider jwtProvider;

  private final RedisTemplate<String, String> redisTemplate;
  private final Authenticator authenticator;
  private final AuthCodeSendEventListener authCodeSendEventListener;
  private final AuthMailSendEventListener authMailSendEventListener;

  @AfterEach
  void cleanEvents() {
    authCodeSendEventListener.clear();
    authMailSendEventListener.clear();
  }

  @Test
  void checkAuthSuccess() {
    PhoneNumber phoneNumber = new PhoneNumber("01012345678");

    // 인증 성공 내역 없는 경우
    assertThatThrownBy(() -> authenticator.checkAuthSuccess(SIGN_UP, phoneNumber))
        .isInstanceOf(ExpiredVerificationPhoneException.class);

    redisTemplate.opsForValue().set("auth:success:01012345678", "-2");

    // 인증 성공 내역이 있는 경우
    assertThatCode(() -> authenticator.checkAuthSuccess(SIGN_UP, phoneNumber))
        .doesNotThrowAnyException();
  }

  @Test
  void sendAuthCode() {
    SendAuthCodeRequest sendAuthCodeRequest = createSendAuthCodeRequest();

    authenticator.sendAuthCode(SIGN_UP, sendAuthCodeRequest);

    // 데이터 검증
    String authAttempt = redisTemplate.opsForValue().get("auth:attempt:sign_up:01012345678");
    String authCode = redisTemplate.opsForValue().get("auth:code:01012345678");
    boolean hasAuthSuccess = requireNonNull(redisTemplate.hasKey("auth:success:01012345678"));

    assertThat(authAttempt).isEqualTo("1");
    assertThat(hasAuthSuccess).isFalse();

    // 도메인 이벤트 검증
    List<AuthCodeSendEvent> events = authCodeSendEventListener.getEvents();
    assertThat(events).hasSize(1);
    assertThat(events.getFirst().phoneNumber()).isEqualTo(new PhoneNumber("01012345678"));
    assertThat(String.valueOf(events.getFirst().code())).isEqualTo(authCode);
  }

  @Test
  void sendAuthCodeImpossible() {
    SendAuthCodeRequest sendAuthCodeRequest = createSendAuthCodeRequest();

    when(accountRepository.exists(any(PhoneNumber.class))).thenReturn(true);

    assertThatThrownBy(() -> authenticator.sendAuthCode(SIGN_UP, sendAuthCodeRequest))
        .isInstanceOf(AlreadyUsePhoneException.class);

    when(accountRepository.existsState(any(PhoneNumber.class), eq(ACTIVE))).thenReturn(false);

    assertThatThrownBy(() -> authenticator.sendAuthCode(CREATE_DEVICE, sendAuthCodeRequest))
        .isInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void sendAuthCodeExceedMaxAttempt() {
    SendAuthCodeRequest sendAuthCodeRequest = createSendAuthCodeRequest();

    redisTemplate.opsForValue().set("auth:attempt:sign_up:01012345678", "5");

    assertThatThrownBy(() -> authenticator.sendAuthCode(SIGN_UP, sendAuthCodeRequest))
        .isInstanceOf(ExceedMaximumVerificationException.class);
  }

  @Test
  void verifyAuthCode() {
    VerifyAuthCodeRequest verifyAuthCodeRequest = createVerifyAuthCodeRequest();

    redisTemplate.opsForValue().set("auth:code:01012345678", "123456");

    PhoneNumber phoneNumber = authenticator.verifyAuthCode(SIGN_UP, verifyAuthCodeRequest);

    boolean hasAuthSuccess = requireNonNull(redisTemplate.hasKey("auth:success:01012345678"));
    boolean hasAuthCode = requireNonNull(redisTemplate.hasKey("auth:code:01012345678"));

    assertThat(phoneNumber.value()).isEqualTo("01012345678");
    assertThat(hasAuthSuccess).isTrue();
    assertThat(hasAuthCode).isFalse();
  }

  @Test
  void verifyAuthCodeFail() {
    VerifyAuthCodeRequest verifyAuthCodeRequest = createVerifyAuthCodeRequest();

    redisTemplate.opsForValue().set("auth:success:01012345678", "-2");

    assertThatThrownBy(() -> authenticator.verifyAuthCode(SIGN_UP, verifyAuthCodeRequest))
        .isInstanceOf(AlreadyVerifiedPhoneException.class);
  }

  @Test
  void sendAuthMail() {
    SendAuthMailRequest sendAuthMailRequest = createSendAuthMailRequest();

    when(accountRepository.existsState(any(Email.class), eq(INACTIVE))).thenReturn(true);

    authenticator.sendAuthMail(sendAuthMailRequest);

    // 도메인 이벤트 검증
    List<AuthMailSendEvent> events = authMailSendEventListener.getEvents();
    assertThat(events).hasSize(1);
    assertThat(events.getFirst().email()).isEqualTo(new Email("admin@everyonewaiter.com"));
  }

  @Test
  void sendAuthMailFail() {
    SendAuthMailRequest sendAuthMailRequest = createSendAuthMailRequest();

    when(accountRepository.existsState(any(Email.class), eq(INACTIVE))).thenReturn(false);

    assertThatThrownBy(() -> authenticator.sendAuthMail(sendAuthMailRequest))
        .isInstanceOf(AlreadyVerifiedEmailException.class);
  }

  @Test
  void verifyAuthMail() {
    JwtPayload payload = new JwtPayload(VERIFICATION_EMAIL_ID, "admin@everyonewaiter.com");

    when(jwtProvider.decode("valid token")).thenReturn(Optional.of(payload));

    Email email = authenticator.verifyAuthMail("valid token");

    assertThat(email.address()).isEqualTo(payload.subject());
  }

  @Test
  void verifyAuthMailFail() {
    JwtPayload payload = new JwtPayload(VERIFICATION_EMAIL_ID - 1, "admin@everyonewaiter.com");

    when(jwtProvider.decode("valid token")).thenReturn(Optional.of(payload));

    assertThatThrownBy(() -> authenticator.verifyAuthMail("valid token"))
        .isInstanceOf(ExpiredVerificationEmailException.class);

    when(jwtProvider.decode("invalid token")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authenticator.verifyAuthMail("invalid token"))
        .isInstanceOf(ExpiredVerificationEmailException.class);
  }

  @Test
  void sendAuthCodeRequestFail() {
    checkValidation(new SendAuthCodeRequest("0551234567"));
  }

  private void checkValidation(SendAuthCodeRequest sendAuthCodeRequest) {
    assertThatThrownBy(() -> authenticator.sendAuthCode(SIGN_UP, sendAuthCodeRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void verifyAuthCodeRequestFail() {
    checkValidation(new VerifyAuthCodeRequest("0551234567", 123456));
    checkValidation(new VerifyAuthCodeRequest("01012345678", 99999));
    checkValidation(new VerifyAuthCodeRequest("01012345678", 1000000));
  }

  private void checkValidation(VerifyAuthCodeRequest verifyAuthCodeRequest) {
    assertThatThrownBy(() -> authenticator.verifyAuthCode(SIGN_UP, verifyAuthCodeRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void sendAuthMailRequestFail() {
    checkValidation(new SendAuthMailRequest("admin@everyonewaiter"));
    checkValidation(new SendAuthMailRequest("admineveryonewaiter"));
    checkValidation(new SendAuthMailRequest("@everyonewaiter"));
  }

  private void checkValidation(SendAuthMailRequest sendAuthMailRequest) {
    assertThatThrownBy(() -> authenticator.sendAuthMail(sendAuthMailRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}
