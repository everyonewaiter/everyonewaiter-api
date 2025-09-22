package com.everyonewaiter.adapter.security;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cors.allow")
@RequiredArgsConstructor
class CorsAllowOrigins {

  private final List<String> origins;

  public List<String> getOrigins() {
    return Collections.unmodifiableList(origins);
  }

}
