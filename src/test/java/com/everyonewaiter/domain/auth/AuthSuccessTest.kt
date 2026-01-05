package com.everyonewaiter.domain.auth;

import static com.everyonewaiter.domain.auth.AuthFixture.createAuthSuccess;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AuthSuccessTest {

  @Test
  void key() {
    AuthSuccess authSuccess = createAuthSuccess();

    assertThat(authSuccess.key()).isEqualTo("auth:success:01012345678");
  }

  @Test
  void value() {
    AuthSuccess authSuccess = createAuthSuccess();

    assertThat(authSuccess.value()).isEqualTo(-2);
  }

  @Test
  void expiration() {
    AuthSuccess authSuccess1 = createAuthSuccess(AuthPurpose.SIGN_UP);
    AuthSuccess authSuccess2 = createAuthSuccess(AuthPurpose.CREATE_DEVICE);

    assertThat(authSuccess1.expiration()).isEqualTo(AuthPurpose.SIGN_UP.getExpiration());
    assertThat(authSuccess2.expiration()).isEqualTo(AuthPurpose.CREATE_DEVICE.getExpiration());
  }

}
