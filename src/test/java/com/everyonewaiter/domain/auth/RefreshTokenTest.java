package com.everyonewaiter.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.domain.shared.AuthenticationException;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

  @Test
  void create() {
    RefreshToken refreshToken = RefreshToken.create(1L);

    assertThat(refreshToken.getAccountId()).isEqualTo(1L);
    assertThat(refreshToken.getCurrentTokenId()).isEqualTo(refreshToken.getId());
  }

  @Test
  void renew() {
    RefreshToken refreshToken = RefreshToken.create(1L);

    assertThat(refreshToken.getCurrentTokenId()).isEqualTo(refreshToken.getId());

    refreshToken.renew(refreshToken.getCurrentTokenId());

    assertThat(refreshToken.getCurrentTokenId()).isNotEqualTo(refreshToken.getId());
  }

  @Test
  void renewFail() {
    RefreshToken refreshToken = RefreshToken.create(1L);

    assertThatThrownBy(() -> refreshToken.renew(999L))
        .isInstanceOf(AuthenticationException.class);
  }

}
