package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.notification.NotificationFixture.createEmailTemplateReader;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

class EmailTemplateTest {

  @Test
  void createContent() {
    EmailTemplate template = EmailTemplate.EMAIL_AUTHENTICATION; // 필요 변수 1개

    HashMap<String, Object> variables = new HashMap<>();
    variables.put("key", "value");

    assertThatCode(() -> template.createContent(createEmailTemplateReader(), variables))
        .doesNotThrowAnyException();
  }

  @Test
  void createContentFail() {
    EmailTemplate template = EmailTemplate.EMAIL_AUTHENTICATION; // 필요 변수 1개

    HashMap<String, Object> variables = new HashMap<>();
    variables.put("key1", "value1");
    variables.put("key2", "value2");

    assertThatThrownBy(() -> template.createContent(createEmailTemplateReader(), variables))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
