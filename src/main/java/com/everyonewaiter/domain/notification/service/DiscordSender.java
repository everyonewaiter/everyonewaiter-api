package com.everyonewaiter.domain.notification.service;

import com.everyonewaiter.domain.notification.service.request.DiscordMessageSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordSender {

  private final DiscordClient discordClient;

  public void send(DiscordMessageSend request) {
    discordClient.send(request);
  }

}
