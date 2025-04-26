package com.everyonewaiter.application.notification.discord.service;

import com.everyonewaiter.domain.notification.discord.DiscordEmbed;
import java.util.List;

public interface DiscordClient {

  void sendMessage(List<DiscordEmbed> embeds);

}
