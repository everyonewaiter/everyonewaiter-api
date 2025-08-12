package com.everyonewaiter.application.auth;

import com.everyonewaiter.application.auth.provided.JwtProvider;
import com.everyonewaiter.application.auth.required.JwtDecoder;
import com.everyonewaiter.application.auth.required.JwtEncoder;
import com.everyonewaiter.domain.auth.JwtPayload;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class JwtProvideService implements JwtProvider {

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  @Override
  public String encode(JwtPayload payload, Duration expiration) {
    return jwtEncoder.encode(payload, expiration);
  }

  @Override
  public Optional<JwtPayload> decode(String token) {
    return jwtDecoder.decode(token);
  }

}
