package com.everyonewaiter.application.notification.provided;

import static com.everyonewaiter.domain.notification.NotificationFixture.createAlimTalkMessage;
import static com.everyonewaiter.domain.notification.NotificationFixture.createDiscordEmbeds;
import static com.everyonewaiter.domain.notification.NotificationFixture.createTemplateEmail;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import com.everyonewaiter.domain.notification.DiscordEmbeds;
import com.everyonewaiter.domain.notification.EmailTemplateReader;
import com.everyonewaiter.domain.notification.TemplateEmail;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

@IntegrationTest
record NotificationSenderTest(
    NotificationSender notificationSender,
    EmailTemplateReader emailTemplateReader
) {

  @Test
  @StdIo
  void sendAlimTalkOneToOne(StdOut stdOut) {
    AlimTalkMessage alimTalkMessage = createAlimTalkMessage();

    notificationSender.sendAlimTalkOneToOne(alimTalkMessage);

    assertThat(stdOut.capturedString())
        .isEqualTo("AlimTalk Sent: " + alimTalkMessage + System.lineSeparator());
  }

  @Test
  @StdIo
  void sendEmailOneToOne(StdOut stdOut) {
    TemplateEmail templateEmail = createTemplateEmail();

    notificationSender.sendEmailOneToOne(templateEmail);

    assertThat(stdOut.capturedLines()[0])
        .isEqualTo("Email Sent: " + templateEmail.toSimpleEmail(emailTemplateReader));
  }

  @Test
  @StdIo
  void sendDiscord(StdOut stdOut) {
    DiscordEmbeds embeds = createDiscordEmbeds();

    notificationSender.sendDiscord(embeds);

    assertThat(stdOut.capturedLines()[0]).isEqualTo("Discord Sent: " + embeds);
  }

}
