package com.everyonewaiter.infrastructure.notification.mail;

import com.everyonewaiter.application.notification.mail.service.MailClient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SimpleMailClient implements MailClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailClient.class);

  private final JavaMailSender javaMailSender;

  @Override
  public void sendTo(String from, String to, String subject, String content) {
    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper =
          new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(content, true);
      javaMailSender.send(mimeMessage);
    } catch (MessagingException exception) {
      LOGGER.error("[메일 전송 실패] from: {}, to: {}, subject: {}, content: {}",
          from, to, subject, content, exception);
    }
  }

}
