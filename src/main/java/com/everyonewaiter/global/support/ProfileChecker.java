package com.everyonewaiter.global.support;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileChecker {

  private final Environment environment;

  public boolean isProduction() {
    return getActiveProfiles().contains("prod");
  }

  public boolean isDevelopment() {
    return getActiveProfiles().contains("dev");
  }

  private List<String> getActiveProfiles() {
    return Arrays.asList(environment.getActiveProfiles());
  }

}
