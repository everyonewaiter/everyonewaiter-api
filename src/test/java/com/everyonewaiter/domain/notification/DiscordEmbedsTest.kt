package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.notification.NotificationFixture.createDiscordEmbed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DiscordEmbedsTest {

  @Test
  void constructor() {
    List<DiscordEmbed> embeds = new ArrayList<>();
    embeds.add(createDiscordEmbed());
    embeds.add(createDiscordEmbed());

    DiscordEmbeds embeds1 = new DiscordEmbeds(embeds);
    DiscordEmbeds embeds2 = new DiscordEmbeds(createDiscordEmbed());

    assertThat(embeds1.embeds()).hasSize(2);
    assertThat(embeds2.embeds()).hasSize(1);
  }

  @Test
  void constructorFail() {
    List<DiscordEmbed> embeds = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      embeds.add(createDiscordEmbed());
    }

    assertThatThrownBy(() -> new DiscordEmbeds(List.of()))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new DiscordEmbeds(embeds))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
