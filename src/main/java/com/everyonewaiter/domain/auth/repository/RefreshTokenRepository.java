package com.everyonewaiter.domain.auth.repository;

import com.everyonewaiter.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository {

  RefreshToken findByIdOrThrow(Long id);

  RefreshToken save(RefreshToken refreshToken);

  void delete(RefreshToken refreshToken);

}
