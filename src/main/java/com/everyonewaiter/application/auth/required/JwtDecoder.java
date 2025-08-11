package com.everyonewaiter.application.auth.required;

import com.everyonewaiter.domain.auth.JwtPayload;
import java.util.Optional;

public interface JwtDecoder {

  Optional<JwtPayload> decode(String token);

}
