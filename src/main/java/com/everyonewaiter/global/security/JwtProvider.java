package com.everyonewaiter.global.security;

import java.time.Duration;
import java.util.Optional;

public interface JwtProvider {

  String generate(JwtPayload payload, Duration expiration);

  Optional<JwtPayload> decode(String token);

}
