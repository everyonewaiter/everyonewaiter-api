package com.everyonewaiter.infrastructure.notification.discord;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "discord")
@RequiredArgsConstructor
class DiscordWebhookClientProperties {

  private final String eventUri;
  private final String loggingUri;

}
