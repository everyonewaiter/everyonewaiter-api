package com.everyonewaiter.application.auth.required;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.auth.RefreshToken;
import com.everyonewaiter.domain.shared.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class RefreshTokenRepositoryTest extends IntegrationTest {

  private final RefreshTokenRepository refreshTokenRepository;

  @Test
  void findByIdOrThrow() {
    RefreshToken refreshToken = createRefreshToken();

    RefreshToken found = refreshTokenRepository.findByIdOrThrow(refreshToken.getId());

    assertThat(found.getId()).isEqualTo(refreshToken.getId());
    assertThat(found.getAccountId()).isEqualTo(refreshToken.getAccountId());
    assertThat(found.getCurrentTokenId()).isEqualTo(refreshToken.getCurrentTokenId());
  }

  @Test
  void findByIdOrThrowFail() {
    assertThatThrownBy(() -> refreshTokenRepository.findByIdOrThrow(999L))
        .isInstanceOf(AuthenticationException.class);
  }

  @Test
  void delete() {
    RefreshToken refreshToken = createRefreshToken();

    refreshTokenRepository.delete(refreshToken);

    assertThatThrownBy(() -> refreshTokenRepository.findByIdOrThrow(refreshToken.getId()))
        .isInstanceOf(AuthenticationException.class);
  }

  private RefreshToken createRefreshToken() {
    RefreshToken refreshToken = RefreshToken.create(1L);

    return refreshTokenRepository.save(refreshToken);
  }

}
