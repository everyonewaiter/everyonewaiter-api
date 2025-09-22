package com.everyonewaiter.domain.notification;

import static org.springframework.util.Assert.isTrue;

import java.util.Collections;
import java.util.List;

public record DiscordEmbeds(List<DiscordEmbed> embeds) {

  public DiscordEmbeds {
    isTrue(!embeds.isEmpty(), "디스코드 임베드는 최소 1개 이상이어야 합니다.");
    isTrue(embeds.size() <= 10, "디스코드 임베드는 10개 이하여야 합니다.");
  }

  public DiscordEmbeds(DiscordEmbed... embeds) {
    this(List.of(embeds));
  }

  @Override
  public List<DiscordEmbed> embeds() {
    return Collections.unmodifiableList(embeds);
  }

}
