package com.everyonewaiter.infrastructure.auth;

import com.everyonewaiter.domain.auth.entity.RefreshToken;
import com.everyonewaiter.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

  private final RefreshTokenJpaRepository refreshTokenJpaRepository;

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    return refreshTokenJpaRepository.save(refreshToken);
  }

}
