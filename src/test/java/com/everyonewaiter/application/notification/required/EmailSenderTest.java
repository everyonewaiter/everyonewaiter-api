package com.everyonewaiter.application.notification.required;

import static com.everyonewaiter.domain.notification.NotificationFixture.createSimpleEmail;
import static org.assertj.core.api.Assertions.assertThat;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.notification.SimpleEmail;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

@IntegrationTest
record EmailSenderTest(EmailSender emailSender) {

  @Test
  @StdIo
  void send(StdOut stdOut) {
    SimpleEmail simpleEmail = createSimpleEmail();

    emailSender.send(simpleEmail);

    assertThat(stdOut.capturedLines()[0]).isEqualTo("Email Sent: " + simpleEmail);
  }

}
