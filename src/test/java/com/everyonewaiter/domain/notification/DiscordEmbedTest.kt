package com.everyonewaiter.domain.notification

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class DiscordEmbedTest {

  @Test
  fun `디스코드 필드 추가`() {
    val embed = createDiscordEmbed()

    embed.addField(DiscordField("이름", "내용"))

    assertThat(embed.fields).hasSize(1)
  }
}
