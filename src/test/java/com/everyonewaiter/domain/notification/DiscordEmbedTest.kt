package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.notification.NotificationFixture.createDiscordEmbed;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DiscordEmbedTest {

  @Test
  void addField() {
    DiscordEmbed embed = createDiscordEmbed();

    embed.addField(new DiscordField("이름", "내용"));

    assertThat(embed.getFields()).hasSize(1);
  }

}
