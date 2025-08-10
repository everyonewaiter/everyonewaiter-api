package com.everyonewaiter.application.notification.required;

import static com.everyonewaiter.domain.notification.NotificationFixture.createAlimTalkMessage;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.notification.AlimTalkMessage;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

@IntegrationTest
record AlimTalkSenderTest(AlimTalkSender alimTalkSender) {

  @Test
  @StdIo
  void send(StdOut stdOut) {
    AlimTalkMessage alimTalkMessage = createAlimTalkMessage();

    alimTalkSender.send(alimTalkMessage);

    assertThat(stdOut.capturedString())
        .isEqualTo("AlimTalk Sent: " + alimTalkMessage + System.lineSeparator());
  }

}
