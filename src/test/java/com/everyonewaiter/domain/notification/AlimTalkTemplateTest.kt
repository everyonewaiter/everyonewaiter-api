package com.everyonewaiter.domain.notification;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AlimTalkTemplateTest {

  @Test
  void createContent() {
    AlimTalkTemplate template = AlimTalkTemplate.AUTHENTICATION_CODE; // 필요 변수 1개

    assertThatCode(() -> template.createContent("value"))
        .doesNotThrowAnyException();
  }

  @Test
  void createContentFail() {
    AlimTalkTemplate template = AlimTalkTemplate.AUTHENTICATION_CODE; // 필요 변수 1개

    assertThatThrownBy(() -> template.createContent("value1", "value2"))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
