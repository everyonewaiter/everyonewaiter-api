package com.everyonewaiter.domain.auth;

import static com.everyonewaiter.domain.auth.AuthFixture.createAuthAttempt;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class AuthAttemptTest {

  @Test
  void isExceed() {
    AuthAttempt authAttempt = createAuthAttempt();

    assertThat(authAttempt.isExceed(0)).isFalse();
    assertThat(authAttempt.isExceed(4)).isFalse();
    assertThat(authAttempt.isExceed(5)).isTrue();
  }

  @Test
  void key() {
    AuthAttempt authAttempt = createAuthAttempt();

    assertThat(authAttempt.key()).isEqualTo("auth:attempt:sign_up:01012345678");
  }

  @Test
  void value() {
    AuthAttempt authAttempt = createAuthAttempt();

    assertThat(authAttempt.value()).isEqualTo(-2);
  }

  @Test
  void expiration() {
    AuthAttempt authAttempt = createAuthAttempt();

    assertThat(authAttempt.expiration()).isEqualTo(Duration.ofDays(1));
  }

}
