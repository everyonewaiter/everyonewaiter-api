package com.everyonewaiter.domain.notification

import com.everyonewaiter.domain.support.WordCounter
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class AlimTalkWeblinkButtonTemplateTest {

  @Test
  fun `알림톡 버튼 생성`() {
    AlimTalkWeblinkButtonTemplate.entries.forEach {
      val parameterCount = WordCounter.count("%s", it.uri)
      if (parameterCount > 0) {
        assertThatCode {
          it.createButton(*Array(parameterCount) { "parameter" })
        }.doesNotThrowAnyException()
      } else {
        assertThatCode { it.createButton() }.doesNotThrowAnyException()
      }
    }
  }

  @Test
  fun `매개변수 개수가 옳바르지 않은 경우 알림톡 버튼 생성 실패`() {
    AlimTalkWeblinkButtonTemplate.entries.forEach {
      val parameterCount = WordCounter.count("%s", it.uri)
      assertThatThrownBy {
        it.createButton(*Array(parameterCount + 1) { "parameter" })
      }.isInstanceOf(IllegalArgumentException::class.java)
    }
  }
}
