package com.everyonewaiter.application.notification.required;

import static com.everyonewaiter.domain.notification.NotificationFixture.createDiscordEmbeds;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

@RequiredArgsConstructor
class DiscordWebhookSenderTest extends IntegrationTest {

  private final DiscordWebhookSender discordWebhookSender;

  @Test
  @StdIo
  void send(StdOut stdOut) {
    DiscordEmbeds embeds = createDiscordEmbeds();

    discordWebhookSender.send(embeds);

    assertThat(stdOut.capturedLines()[0]).isEqualTo("Discord Sent: " + embeds);
  }

}
