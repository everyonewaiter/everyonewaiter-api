package com.everyonewaiter.adapter.integration.notification;

import com.everyonewaiter.application.notification.required.DiscordWebhookSender;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DiscordWebhookClientImpl implements DiscordWebhookSender {

  private final DiscordChannel discordChannel;
  private final DiscordWebhookClient discordWebhookClient;

  @Override
  public void send(DiscordEmbeds discordEmbeds) {
    discordWebhookClient.send(discordChannel.getEvent(), discordEmbeds);
  }

}
