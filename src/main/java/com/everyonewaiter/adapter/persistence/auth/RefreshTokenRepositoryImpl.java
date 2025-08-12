package com.everyonewaiter.adapter.persistence.auth;

import com.everyonewaiter.application.auth.required.RefreshTokenRepository;
import com.everyonewaiter.domain.auth.RefreshToken;
import com.everyonewaiter.domain.shared.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

  private final RefreshTokenJpaRepository refreshTokenJpaRepository;

  @Override
  public RefreshToken findByIdOrThrow(Long refreshTokenId) {
    return refreshTokenJpaRepository.findById(refreshTokenId)
        .orElseThrow(AuthenticationException::new);
  }

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    return refreshTokenJpaRepository.save(refreshToken);
  }

  @Override
  public void delete(RefreshToken refreshToken) {
    refreshTokenJpaRepository.delete(refreshToken);
  }

}
