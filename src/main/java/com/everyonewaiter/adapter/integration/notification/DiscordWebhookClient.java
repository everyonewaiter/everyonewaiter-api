package com.everyonewaiter.adapter.integration.notification;

import com.everyonewaiter.domain.notification.DiscordEmbeds;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discordWebhookClient", url = "https://discord.com/api/webhooks")
interface DiscordWebhookClient {

  @PostMapping(value = "/{discordWebhookUri}", consumes = MediaType.APPLICATION_JSON_VALUE)
  void send(
      @PathVariable String discordWebhookUri,
      @RequestBody DiscordEmbeds discordEmbeds
  );

}
