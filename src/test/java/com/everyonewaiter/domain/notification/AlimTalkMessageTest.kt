package com.everyonewaiter.domain.notification

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AlimTalkMessageTest {

  @Test
  fun `알림톡 버튼 추가`() {
    val message = createAlimTalkMessage()

    message.addButton(AlimTalkWeblinkButtonTemplate.MENU_PREVIEW, 1L)

    assertThat(message.buttons).hasSize(1)
  }
}
