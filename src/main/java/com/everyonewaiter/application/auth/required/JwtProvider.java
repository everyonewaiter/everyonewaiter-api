package com.everyonewaiter.application.auth.required;

import com.everyonewaiter.domain.auth.JwtPayload;
import java.time.Duration;
import java.util.Optional;

public interface JwtProvider {

  String encode(JwtPayload payload, Duration expiration);

  Optional<JwtPayload> decode(String token);

}
