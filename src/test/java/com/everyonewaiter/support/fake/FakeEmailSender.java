package com.everyonewaiter.support.fake;

import com.everyonewaiter.application.notification.required.EmailSender;
import com.everyonewaiter.domain.notification.SimpleEmail;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeEmailSender implements EmailSender {

  private static final Logger log = LoggerFactory.getLogger(FakeEmailSender.class);

  private final List<SimpleEmail> sentEmails = new ArrayList<>();

  @Override
  public void send(SimpleEmail simpleEmail) {
    log.info("[FAKE 이메일 발송] from: {}, to: {}, subject: {}, content: {}",
        simpleEmail.from(),
        simpleEmail.to(),
        simpleEmail.subject(),
        simpleEmail.content()
    );
    sentEmails.add(simpleEmail);
  }

  public void clear() {
    sentEmails.clear();
  }

  public List<SimpleEmail> getSentEmails() {
    return Collections.unmodifiableList(sentEmails);
  }

}
