package com.everyonewaiter.application.auth.required;

import com.everyonewaiter.domain.auth.RefreshToken;

public interface RefreshTokenRepository {

  RefreshToken findByIdOrThrow(Long refreshTokenId);

  RefreshToken save(RefreshToken refreshToken);

  void delete(RefreshToken refreshToken);

}
