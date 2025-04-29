package com.everyonewaiter.application.notification;

import com.everyonewaiter.application.notification.request.DiscordMessageSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordService {

  private final DiscordClient discordClient;

  public void sendMessage(DiscordMessageSend request) {
    discordClient.sendMessage(request.embeds());
  }

}
