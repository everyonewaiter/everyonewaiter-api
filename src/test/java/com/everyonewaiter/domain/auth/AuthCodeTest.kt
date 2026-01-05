package com.everyonewaiter.domain.auth;

import static com.everyonewaiter.domain.auth.AuthFixture.createAuthCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class AuthCodeTest {

  @Test
  void verify() {
    AuthCode authCode = createAuthCode();

    assertThatCode(() -> authCode.verify(123456)).doesNotThrowAnyException();
  }

  @Test
  void verifyFail() {
    AuthCode authCode = createAuthCode();

    assertThatThrownBy(() -> authCode.verify(0))
        .isInstanceOf(ExpiredVerificationCodeException.class);
    assertThatThrownBy(() -> authCode.verify(999999))
        .isInstanceOf(UnmatchedVerificationCodeException.class);
  }

  @Test
  void key() {
    AuthCode authCode = createAuthCode();

    assertThat(authCode.key()).isEqualTo("auth:code:01012345678");
  }

  @Test
  void value() {
    AuthCode authCode = createAuthCode();

    assertThat(authCode.value()).isEqualTo(123456);
  }

  @Test
  void expiration() {
    AuthCode authCode = createAuthCode();

    assertThat(authCode.expiration()).isEqualTo(Duration.ofMinutes(5));
  }

}
