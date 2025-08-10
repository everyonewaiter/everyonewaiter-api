package com.everyonewaiter.adapter.integration.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "naver.sens")
@RequiredArgsConstructor
class NaverSensProperties {

  private final String accessKey;
  private final String secretKey;
  private final String serviceId;
  private final String channelId;

}
