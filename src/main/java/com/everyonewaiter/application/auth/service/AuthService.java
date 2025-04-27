package com.everyonewaiter.application.auth.service;

import com.everyonewaiter.application.auth.service.request.SendAuthCode;
import com.everyonewaiter.application.auth.service.request.VerifyAuthCode;
import com.everyonewaiter.domain.auth.entity.AuthAttempt;
import com.everyonewaiter.domain.auth.entity.AuthCode;
import com.everyonewaiter.domain.auth.entity.AuthPurpose;
import com.everyonewaiter.domain.auth.entity.AuthSuccess;
import com.everyonewaiter.domain.auth.event.AuthCodeSendEvent;
import com.everyonewaiter.domain.auth.event.AuthMailSendEvent;
import com.everyonewaiter.domain.auth.repository.AuthRepository;
import com.everyonewaiter.domain.auth.service.AuthValidator;
import com.everyonewaiter.global.security.JwtPayload;
import com.everyonewaiter.global.security.JwtProvider;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtProvider jwtProvider;
  private final AuthValidator authValidator;
  private final AuthRepository authRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  public void checkExistsAuthSuccess(String phoneNumber, AuthPurpose purpose) {
    AuthSuccess authSuccess = new AuthSuccess(phoneNumber, purpose);
    authValidator.checkExistsAuthSuccess(authSuccess);
  }

  public void sendAuthCode(SendAuthCode request) {
    AuthAttempt authAttempt = new AuthAttempt(request.phoneNumber(), request.purpose());
    authValidator.validateAuthAttempt(authAttempt);

    AuthCode authCode = new AuthCode(request.phoneNumber());
    authRepository.save(authCode);
    authRepository.increment(authAttempt);
    authRepository.delete(new AuthSuccess(request.phoneNumber(), request.purpose()));

    AuthCodeSendEvent event = new AuthCodeSendEvent(request.phoneNumber(), authCode.code());
    applicationEventPublisher.publishEvent(event);
  }

  public void verifyAuthCode(VerifyAuthCode request) {
    AuthSuccess authSuccess = new AuthSuccess(request.phoneNumber(), request.purpose());
    authValidator.checkNotExistsAuthSuccess(authSuccess);

    AuthCode authCode = new AuthCode(request.phoneNumber(), request.code());
    int code = authRepository.find(authCode);
    authCode.verify(code);

    authRepository.save(authSuccess);
    authRepository.delete(authCode);
  }

  public String generateToken(JwtPayload payload, Duration expiration) {
    return jwtProvider.generate(payload, expiration);
  }

  @Transactional(readOnly = true)
  public void sendAuthMail(String email) {
    applicationEventPublisher.publishEvent(new AuthMailSendEvent(email));
  }

}
