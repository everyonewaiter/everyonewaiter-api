package com.everyonewaiter.domain.notification.service.request;

import com.everyonewaiter.domain.notification.DiscordEmbed;
import java.util.List;

public record DiscordMessageSend(List<DiscordEmbed> embeds) {

  public DiscordMessageSend(DiscordEmbed embed) {
    this(List.of(embed));
  }

}
