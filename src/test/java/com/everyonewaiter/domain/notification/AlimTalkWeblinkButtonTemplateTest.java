package com.everyonewaiter.domain.notification;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AlimTalkWeblinkButtonTemplateTest {

  @Test
  void getUri() {
    var template = AlimTalkWeblinkButtonTemplate.MENU_PREVIEW; // 필요 변수 1개

    assertThatCode(() -> template.createButton("value1"))
        .doesNotThrowAnyException();
  }

  @Test
  void getUriFail() {
    var template = AlimTalkWeblinkButtonTemplate.MENU_PREVIEW; // 필요 변수 1개

    assertThatThrownBy(() -> template.createButton("value1", "value2"))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
