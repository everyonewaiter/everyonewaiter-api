package com.everyonewaiter.domain.notification.service.request;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MailSend {

  @Builder.Default
  private final String from = "noreply@everyonewaiter.com";
  private final String to;
  private final String subject;
  private final String templateName;
  @Singular("variable")
  private final Map<String, Object> variables;

}
