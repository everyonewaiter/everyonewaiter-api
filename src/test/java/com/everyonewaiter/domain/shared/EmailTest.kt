package com.everyonewaiter.domain.shared

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class EmailTest {

  @Test
  fun `이메일 생성`() {
    val address = "admin@everyonewaiter.com"

    val email = Email(address)

    assertThat(email.address).isEqualTo(address)
  }

  @Test
  fun `이메일 형식이 옳바르지 않은 경우 생성 실패`() {
    val address = "admin-everyonewaiter.com"

    assertThatThrownBy { Email(address) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
