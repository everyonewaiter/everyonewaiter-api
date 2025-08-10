package com.everyonewaiter.adapter.integration.notification;

import com.everyonewaiter.application.notification.required.DiscordWebhookSender;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DiscordWebhookClientImpl implements DiscordWebhookSender {

  private final DiscordWebhookClient discordWebhookClient;
  private final DiscordWebhookClientProperties discordWebhookClientProperties;

  @Override
  public void send(DiscordEmbeds discordEmbeds) {
    discordWebhookClient.send(discordWebhookClientProperties.getEventUri(), discordEmbeds);
  }

}
