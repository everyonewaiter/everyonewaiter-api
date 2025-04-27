package com.everyonewaiter.domain.auth.service;

import com.everyonewaiter.domain.auth.entity.AuthAttempt;
import com.everyonewaiter.domain.auth.entity.AuthSuccess;
import com.everyonewaiter.domain.auth.repository.AuthRepository;
import com.everyonewaiter.domain.auth.repository.RefreshTokenRepository;
import com.everyonewaiter.global.exception.AuthenticationException;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.security.JwtFixedId;
import com.everyonewaiter.global.security.JwtPayload;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidator {

  private final AuthRepository authRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  public void validateAuthAttempt(AuthAttempt authAttempt) {
    if (authAttempt.isExceed(authRepository.find(authAttempt))) {
      throw new BusinessException(ErrorCode.EXCEED_MAXIMUM_VERIFICATION_PHONE_NUMBER);
    }
  }

  public void checkExistsAuthSuccess(AuthSuccess authSuccess) {
    if (!authRepository.exists(authSuccess)) {
      throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_PHONE_NUMBER);
    }
  }

  public void checkNotExistsAuthSuccess(AuthSuccess authSuccess) {
    if (authRepository.exists(authSuccess)) {
      throw new BusinessException(ErrorCode.ALREADY_VERIFIED_PHONE_NUMBER);
    }
  }

  public void validateAuthMailTokenPayload(JwtPayload payload) {
    if (!Objects.equals(payload.id(), JwtFixedId.VERIFICATION_EMAIL.getId())) {
      throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_EMAIL);
    }
  }

  public void validateAliveAccountToken(JwtPayload payload) {
    try {
      Long currentTokenId = Long.valueOf(payload.subject());
      if (!refreshTokenRepository.aliveAccountToken(payload.id(), currentTokenId)) {
        throw new AuthenticationException();
      }
    } catch (NumberFormatException exception) {
      throw new AuthenticationException();
    }
  }

}
