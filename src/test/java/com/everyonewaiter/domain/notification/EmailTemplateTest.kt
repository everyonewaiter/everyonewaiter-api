package com.everyonewaiter.domain.notification

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class EmailTemplateTest {

  @Test
  fun `이메일 컨텐츠 생성`() {
    val templateReader = createEmailTemplateReader()

    EmailTemplate.entries.forEach {
      val variables = mutableMapOf<String, Any>()
      for (i in 1..it.variableCount) {
        variables["key-$i"] = "value-$i"
      }

      assertThatCode {
        it.createContent(templateReader, variables)
      }.doesNotThrowAnyException()
    }
  }

  @Test
  fun `매개변수 개수가 옳바르지 않은 경우 이메일 컨텐츠 생성 실패`() {
    val templateReader = createEmailTemplateReader()

    EmailTemplate.entries.forEach {
      val variables = mutableMapOf<String, Any>()
      for (i in 1..it.variableCount + 1) {
        variables["key-$i"] = "value-$i"
      }

      assertThatThrownBy {
        it.createContent(templateReader, variables)
      }.isInstanceOf(IllegalArgumentException::class.java)
    }
  }
}
