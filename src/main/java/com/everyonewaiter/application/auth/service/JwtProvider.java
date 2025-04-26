package com.everyonewaiter.application.auth.service;

import java.time.Duration;
import java.util.Optional;

public interface JwtProvider {

  String generate(JwtPayload payload);

  String generate(JwtPayload payload, Duration expiration);

  Optional<JwtPayload> decode(String token);

}
