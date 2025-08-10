package com.everyonewaiter.domain.notification;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AlimTalkWeblinkButtonTemplateTest {

  @Test
  void getLink() {
    var template = AlimTalkWeblinkButtonTemplate.MENU_PREVIEW; // 필요 변수 2개

    assertThatCode(() -> template.createButton("value1", "value2"))
        .doesNotThrowAnyException();
  }

  @Test
  void getLinkFail() {
    var template = AlimTalkWeblinkButtonTemplate.MENU_PREVIEW; // 필요 변수 2개

    assertThatThrownBy(() -> template.createButton("value1"))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
