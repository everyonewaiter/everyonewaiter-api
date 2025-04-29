package com.everyonewaiter.application.notification;

import com.everyonewaiter.application.notification.request.MailSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final MailClient mailClient;
  private final MailTemplateReader mailTemplateReader;

  public void sendMail(MailSend request) {
    String content = mailTemplateReader.read(request.getTemplateName(), request.getVariables());
    mailClient.sendTo(request.getFrom(), request.getTo(), request.getSubject(), content);
  }

}
