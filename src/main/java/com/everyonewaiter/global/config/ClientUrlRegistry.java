package com.everyonewaiter.global.config;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client")
@RequiredArgsConstructor
public class ClientUrlRegistry {

  private final List<String> urls;

  public List<String> getUrls() {
    return Collections.unmodifiableList(urls);
  }

}
