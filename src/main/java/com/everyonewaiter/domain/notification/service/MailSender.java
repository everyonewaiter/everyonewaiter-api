package com.everyonewaiter.domain.notification.service;

import com.everyonewaiter.domain.notification.service.request.MailSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component(value = "emailSender")
@RequiredArgsConstructor
public class MailSender {

  private final MailClient mailClient;
  private final MailTemplateReader mailTemplateReader;

  public void sendTo(MailSend mailSend) {
    validateRequest(mailSend);
    String content = mailTemplateReader.read(mailSend.getTemplateName(), mailSend.getVariables());
    mailClient.sendTo(mailSend.getFrom(), mailSend.getTo(), mailSend.getSubject(), content);
  }

  private void validateRequest(MailSend mailSend) {
    Assert.hasText(mailSend.getFrom(), "메일 발신자는 공백일 수 없습니다.");
    Assert.hasText(mailSend.getTo(), "메일 수신자는 공백일 수 없습니다.");
    Assert.hasText(mailSend.getTemplateName(), "메일 템플릿 이름은 공백일 수 없습니다.");
    Assert.hasText(mailSend.getSubject(), "메일의 제목은 공백일 수 없습니다.");
  }

}
