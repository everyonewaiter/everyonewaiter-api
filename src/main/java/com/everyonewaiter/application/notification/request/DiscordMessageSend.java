package com.everyonewaiter.application.notification.request;

import com.everyonewaiter.domain.notification.discord.DiscordEmbed;
import java.util.List;

public record DiscordMessageSend(List<DiscordEmbed> embeds) {

  public DiscordMessageSend(DiscordEmbed embed) {
    this(List.of(embed));
  }

}
