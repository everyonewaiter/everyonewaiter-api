package com.everyonewaiter.support.fake;

import com.everyonewaiter.application.notification.required.DiscordWebhookSender;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeDiscordWebhookSender implements DiscordWebhookSender {

  private static final Logger log = LoggerFactory.getLogger(FakeDiscordWebhookSender.class);

  private final List<DiscordEmbeds> sentEmbeds = new ArrayList<>();

  @Override
  public void send(DiscordEmbeds discordEmbeds) {
    log.info("[FAKE 디스코드 웹훅] embeds 수: {}, embeds: {}",
        discordEmbeds.embeds().size(),
        discordEmbeds
    );
    sentEmbeds.add(discordEmbeds);
  }

  public void clear() {
    sentEmbeds.clear();
  }

  public List<DiscordEmbeds> getSentEmbeds() {
    return Collections.unmodifiableList(sentEmbeds);
  }

}
