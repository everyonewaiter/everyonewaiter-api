package com.everyonewaiter.adapter.integration.notification;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.notification.required.EmailSender;
import com.everyonewaiter.domain.notification.SimpleEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.annotation.Fallback;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Fallback
@Component
@RequiredArgsConstructor
class SimpleEmailSender implements EmailSender {

  private static final Logger LOGGER = getLogger(SimpleEmailSender.class);

  private static final String FROM_FORMAT = "모두의 웨이터 <%s>";

  private final JavaMailSender javaMailSender;

  @Override
  public void send(SimpleEmail simpleEmail) {
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, UTF_8.name());

      mimeMessageHelper.setFrom(FROM_FORMAT.formatted(simpleEmail.from()));
      mimeMessageHelper.setTo(simpleEmail.to());
      mimeMessageHelper.setSubject(simpleEmail.subject());
      mimeMessageHelper.setText(simpleEmail.content(), true);

      javaMailSender.send(mimeMessage);
    } catch (MessagingException exception) {
      LOGGER.error("[메일 전송 실패] from: {}, to: {}, subject: {}",
          simpleEmail.from(), simpleEmail.to(), simpleEmail.subject(), exception);
    }
  }

}
