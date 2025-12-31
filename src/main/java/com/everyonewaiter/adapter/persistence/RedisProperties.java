package com.everyonewaiter.adapter.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "spring.data.redis")
@RequiredArgsConstructor
class RedisProperties {

  private final String host;
  private final int port;
  private final String password;

}
