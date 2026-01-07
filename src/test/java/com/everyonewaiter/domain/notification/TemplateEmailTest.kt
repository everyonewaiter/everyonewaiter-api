package com.everyonewaiter.domain.notification

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TemplateEmailTest {

  @Test
  fun `이메일 템플릿 변수 추가`() {
    val templateEmail = createTemplateEmail(variables = mutableMapOf())

    templateEmail.addTemplateVariable("key", "value")

    assertThat(templateEmail.templateVariables).hasSize(1)
  }

  @Test
  fun `심플 이메일로 변환`() {
    val templateEmail = createTemplateEmail()

    val simpleEmail = templateEmail.toSimpleEmail(createEmailTemplateReader())

    assertThat(simpleEmail.subject).isEqualTo(templateEmail.subject)
    assertThat(simpleEmail.from).isEqualTo(templateEmail.from.address)
    assertThat(simpleEmail.to).isEqualTo(templateEmail.to.address)
    templateEmail.templateVariables.forEach { (_, value) ->
      assertThat(simpleEmail.content).contains(value.toString())
    }
  }
}
