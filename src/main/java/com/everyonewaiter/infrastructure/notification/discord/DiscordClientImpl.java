package com.everyonewaiter.infrastructure.notification.discord;

import com.everyonewaiter.domain.notification.service.DiscordClient;
import com.everyonewaiter.domain.notification.service.request.DiscordMessageSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DiscordClientImpl implements DiscordClient {

  private final DiscordWebhookClient discordWebhookClient;
  private final DiscordWebhookClientProperties discordWebhookClientProperties;

  @Override
  public void send(DiscordMessageSend request) {
    discordWebhookClient.send(discordWebhookClientProperties.getEventUri(), request);
  }

}
