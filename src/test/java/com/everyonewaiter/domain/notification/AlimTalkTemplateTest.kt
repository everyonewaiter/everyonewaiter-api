package com.everyonewaiter.domain.notification

import com.everyonewaiter.domain.support.WordCounter
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class AlimTalkTemplateTest {

  @Test
  fun `알림톡 컨텐츠 생성`() {
    AlimTalkTemplate.entries.forEach {
      val parameterCount = WordCounter.count("%s", it.templateContent)
      if (parameterCount > 0) {
        assertThatCode {
          it.createContent(*Array(parameterCount) { "parameter" })
        }.doesNotThrowAnyException()
      } else {
        assertThatCode { it.createContent() }.doesNotThrowAnyException()
      }
    }
  }

  @Test
  fun `매개변수 개수가 옳바르지 않은 경우 알림톡 컨텐츠 생성 실패`() {
    AlimTalkTemplate.entries.forEach {
      val parameterCount = WordCounter.count("%s", it.templateContent)
      assertThatThrownBy {
        it.createContent(*Array(parameterCount + 1) { "parameter" })
      }.isInstanceOf(IllegalArgumentException::class.java)
    }
  }
}
