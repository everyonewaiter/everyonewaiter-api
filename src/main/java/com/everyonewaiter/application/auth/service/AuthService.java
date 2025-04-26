package com.everyonewaiter.application.auth.service;

import com.everyonewaiter.application.auth.service.request.SendAuthCode;
import com.everyonewaiter.domain.auth.entity.AuthAttempt;
import com.everyonewaiter.domain.auth.entity.AuthCode;
import com.everyonewaiter.domain.auth.event.AuthCodeSendEvent;
import com.everyonewaiter.domain.auth.repository.AuthRepository;
import com.everyonewaiter.domain.auth.service.AuthValidator;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtProvider jwtProvider;
  private final AuthValidator authValidator;
  private final AuthRepository authRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  public void sendAuthCode(SendAuthCode request) {
    AuthAttempt authAttempt = AuthAttempt.create(request.getPhoneNumber(), request.getPurpose());
    authValidator.validateAuthAttempt(authAttempt);

    AuthCode authCode = AuthCode.create(request.getPhoneNumber());
    authRepository.save(authCode);
    authRepository.increment(authAttempt);

    AuthCodeSendEvent event = new AuthCodeSendEvent(request.getPhoneNumber(), authCode.getCode());
    applicationEventPublisher.publishEvent(event);
  }

  public String generateToken(JwtPayload payload, Duration expiration) {
    return jwtProvider.generate(payload, expiration);
  }

}
