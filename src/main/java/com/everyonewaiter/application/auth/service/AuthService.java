package com.everyonewaiter.application.auth.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtProvider jwtProvider;

  public String generateToken(JwtPayload payload, Duration expiration) {
    return jwtProvider.generate(payload, expiration);
  }

}
