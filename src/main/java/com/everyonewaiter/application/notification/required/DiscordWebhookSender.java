package com.everyonewaiter.application.notification.required;

import com.everyonewaiter.domain.notification.DiscordEmbeds;

public interface DiscordWebhookSender {

  void send(DiscordEmbeds discordEmbeds);

}
