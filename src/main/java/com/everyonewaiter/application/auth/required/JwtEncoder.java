package com.everyonewaiter.application.auth.required;

import com.everyonewaiter.domain.auth.JwtPayload;
import java.time.Duration;

public interface JwtEncoder {

  String encode(JwtPayload payload, Duration expiration);

}
