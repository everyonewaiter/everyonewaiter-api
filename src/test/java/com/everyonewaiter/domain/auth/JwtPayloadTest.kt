package com.everyonewaiter.domain.auth

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class JwtPayloadTest {

  @Test
  fun `JWT 페이로드의 ID가 숫자인 경우 파싱`() {
    val payload1 = JwtPayload("1", "subject")
    val payload2 = JwtPayload(2L, "subject")

    assertThat(payload1.longId).isEqualTo(1)
    assertThat(payload2.longId).isEqualTo(2)
  }

  @Test
  fun `JWT 페이로드의 ID가 숫자가 아닌 경우 파싱 실패`() {
    val payload = JwtPayload("id", "subject")

    assertThatThrownBy { payload.longId }.isInstanceOf(NumberFormatException::class.java)
  }
}
