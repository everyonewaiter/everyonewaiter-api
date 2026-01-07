package com.everyonewaiter.domain.notification

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class DiscordEmbedsTest {

  @Test
  fun `디스코드 임베드 생성`() {
    val embeds1 = DiscordEmbeds(createDiscordEmbed(), createDiscordEmbed())

    assertThat(embeds1.embeds).hasSize(2)
  }

  @Test
  fun `디스코드 임베드의 개수가 0이거나 10을 초과하면 생성 실패`() {
    assertThatThrownBy {
      DiscordEmbeds(emptyList())
    }.isInstanceOf(IllegalArgumentException::class.java)

    assertThatThrownBy {
      DiscordEmbeds(List(11) { createDiscordEmbed() })
    }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
