package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.notification.NotificationFixture.createEmailTemplateReader;
import static com.everyonewaiter.domain.notification.NotificationFixture.createTemplateEmail;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TemplateEmailTest {

  @Test
  void addTemplateVariable() {
    TemplateEmail templateEmail = createTemplateEmail(); // 템플릿 변수 1개

    templateEmail.addTemplateVariable("key", "value");

    assertThat(templateEmail.getTemplateVariables()).hasSize(2);
  }

  @Test
  void toSimpleEmail() {
    TemplateEmail templateEmail = createTemplateEmail();

    SimpleEmail simpleEmail = templateEmail.toSimpleEmail(createEmailTemplateReader());

    assertThat(simpleEmail.from()).isEqualTo(templateEmail.getFrom().address());
    assertThat(simpleEmail.to()).isEqualTo(templateEmail.getTo().address());
    assertThat(simpleEmail.subject()).isEqualTo(templateEmail.getSubject());

    for (Object variable : templateEmail.getTemplateVariables().values()) {
      assertThat(simpleEmail.content()).contains(variable.toString());
    }
  }

}
