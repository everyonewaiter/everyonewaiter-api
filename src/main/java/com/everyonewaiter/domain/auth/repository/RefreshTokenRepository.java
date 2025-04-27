package com.everyonewaiter.domain.auth.repository;

import com.everyonewaiter.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository {

  boolean aliveAccountToken(Long accountId, Long currentTokenId);

  RefreshToken findByIdOrThrow(Long id);

  RefreshToken save(RefreshToken refreshToken);

  void delete(RefreshToken refreshToken);

}
