package com.everyonewaiter.domain.auth.service;

import com.everyonewaiter.domain.auth.entity.AuthAttempt;
import com.everyonewaiter.domain.auth.repository.AuthRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
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

}
