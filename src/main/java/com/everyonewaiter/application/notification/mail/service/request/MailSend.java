package com.everyonewaiter.application.notification.mail.service.request;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailSend {

  private static final String FROM_FORMAT = "모두의 웨이터 <%s>";
  private static final String NO_REPLY_FROM = "noreply@everyonewaiter.com";

  private String from;
  private String to;
  private String subject;
  private String templateName;
  private final Map<String, Object> variables = new HashMap<>();

  private MailSend(String from, String to, String templateName, String subject) {
    validate(from, to, templateName, subject);
    this.from = from;
    this.to = to;
    this.templateName = templateName;
    this.subject = subject;
  }

  private void validate(String from, String to, String templateName, String subject) {
    Assert.hasText(from, "메일 발신자는 공백일 수 없습니다.");
    Assert.hasText(to, "메일 수신자는 공백일 수 없습니다.");
    Assert.hasText(templateName, "메일 템플릿 이름은 공백일 수 없습니다.");
    Assert.hasText(subject, "메일의 제목은 공백일 수 없습니다.");
  }

  public static MailSend of(String to, String templateName, String subject) {
    return of(NO_REPLY_FROM, to, templateName, subject);
  }

  public static MailSend of(
      String to,
      String templateName,
      String subject,
      Map<String, Object> variables
  ) {
    return of(NO_REPLY_FROM, to, templateName, subject, variables);
  }

  public static MailSend of(String from, String to, String templateName, String subject) {
    return of(from, to, templateName, subject, Map.of());
  }

  public static MailSend of(
      String from,
      String to,
      String templateName,
      String subject,
      Map<String, Object> variables
  ) {
    MailSend mailSend = new MailSend(FROM_FORMAT.formatted(from), to, templateName, subject);
    mailSend.variables.putAll(variables);
    return mailSend;
  }

}
