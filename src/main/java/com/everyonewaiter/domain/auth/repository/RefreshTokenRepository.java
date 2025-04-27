package com.everyonewaiter.domain.auth.repository;

import com.everyonewaiter.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository {

  RefreshToken save(RefreshToken refreshToken);

}
