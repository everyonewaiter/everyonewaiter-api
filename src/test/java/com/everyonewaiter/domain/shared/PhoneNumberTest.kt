package com.everyonewaiter.domain.shared

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class PhoneNumberTest {

  @Test
  fun `휴대폰 번호 생성`() {
    val value = "01012345678"

    val phoneNumber = PhoneNumber(value)

    assertThat(phoneNumber.value).isEqualTo(value)
  }

  @Test
  fun `휴대폰 번호 형식이 옳바르지 않은 경우 생성 실패`() {
    val value = "010-1234-5678"

    assertThatThrownBy { PhoneNumber(value) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
