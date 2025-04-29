package com.everyonewaiter.domain.notification.service;

import com.everyonewaiter.domain.notification.service.request.DiscordMessageSend;

public interface DiscordClient {

  void send(DiscordMessageSend request);

}
