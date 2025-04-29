package com.everyonewaiter.application.notification;

import com.everyonewaiter.domain.notification.discord.DiscordEmbed;
import java.util.List;

public interface DiscordClient {

  void sendMessage(List<DiscordEmbed> embeds);

}
