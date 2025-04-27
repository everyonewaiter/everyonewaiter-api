package com.everyonewaiter.infrastructure.auth;

import com.everyonewaiter.domain.auth.entity.RefreshToken;
import com.everyonewaiter.domain.auth.repository.RefreshTokenRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

  private final RefreshTokenJpaRepository refreshTokenJpaRepository;

  @Override
  public RefreshToken findByIdOrThrow(Long id) {
    return refreshTokenJpaRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
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
