package com.everyonewaiter.domain.auth.service;

import com.everyonewaiter.domain.auth.JwtFixedId;
import com.everyonewaiter.domain.auth.JwtPayload;
import com.everyonewaiter.domain.auth.entity.AuthAttempt;
import com.everyonewaiter.domain.auth.entity.AuthSuccess;
import com.everyonewaiter.domain.auth.repository.AuthRepository;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidator {

  private final AuthRepository authRepository;

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
    if (!JwtFixedId.VERIFICATION_EMAIL_ID.equals(payload.id())) {
      throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_EMAIL);
    }
  }

}
