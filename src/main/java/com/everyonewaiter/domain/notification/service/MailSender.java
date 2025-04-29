package com.everyonewaiter.domain.notification.service;

import com.everyonewaiter.domain.notification.service.request.MailSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSender {

  private final MailClient mailClient;
  private final MailTemplateReader mailTemplateReader;

  public void sendTo(MailSend mailSend) {
    String content = mailTemplateReader.read(mailSend.getTemplateName(), mailSend.getVariables());
    mailClient.sendTo(mailSend.getFrom(), mailSend.getTo(), mailSend.getSubject(), content);
  }

}
