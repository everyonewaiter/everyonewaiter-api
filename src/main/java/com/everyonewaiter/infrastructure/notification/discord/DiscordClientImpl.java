package com.everyonewaiter.infrastructure.notification.discord;

import com.everyonewaiter.application.notification.DiscordClient;
import com.everyonewaiter.domain.notification.discord.DiscordEmbed;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DiscordClientImpl implements DiscordClient {

  private final DiscordWebhookClient discordWebhookClient;
  private final DiscordWebhookClientProperties discordWebhookClientProperties;

  @Override
  public void sendMessage(List<DiscordEmbed> embeds) {
    DiscordWebhookRequest request = new DiscordWebhookRequest(embeds);
    discordWebhookClient.sendMessage(discordWebhookClientProperties.getEventUri(), request);
  }

}
