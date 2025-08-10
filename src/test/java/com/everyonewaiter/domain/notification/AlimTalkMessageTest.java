package com.everyonewaiter.domain.notification;

import static com.everyonewaiter.domain.notification.AlimTalkWeblinkButtonTemplate.MENU_PREVIEW;
import static com.everyonewaiter.domain.notification.NotificationFixture.createAlimTalkMessage;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AlimTalkMessageTest {

  @Test
  void addButton() {
    AlimTalkMessage alimTalkMessage = createAlimTalkMessage();

    alimTalkMessage.addButton(MENU_PREVIEW, "https://everyonewaiter.com", 1L);

    assertThat(alimTalkMessage.getButtons()).hasSize(1);
  }

}
