package com.everyonewaiter.adapter.integration.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "discord.channel")
@RequiredArgsConstructor
class DiscordChannel {

  private final String event;
  private final String error;

}
