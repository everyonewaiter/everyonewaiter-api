package com.everyonewaiter.domain.notification;

import static org.springframework.util.Assert.isTrue;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailTemplate {

  EMAIL_AUTHENTICATION("email-authentication", 1),
  STORE_REGISTRATION_APPROVE("store-registration-approve", 2),
  STORE_REGISTRATION_REJECT("store-registration-reject", 3),
  ;

  private final String templateName;
  private final int variableCount;

  public String createContent(
      EmailTemplateReader emailTemplateReader,
      Map<String, Object> templateVariables
  ) {
    isTrue(variableCount == templateVariables.size(), "이메일 템플릿 변수 설정이 옳바르지 않습니다.");

    return emailTemplateReader.read(templateName, templateVariables);
  }

}
