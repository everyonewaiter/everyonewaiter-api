package com.everyonewaiter.domain.auth

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class JwtPayloadTest {

  @Test
  fun `JWT 페이로드의 제목은 숫자 또는 문자열로 생성 가능`() {
    assertThatCode { JwtPayload(1L, "subject") }.doesNotThrowAnyException()
    assertThatCode { JwtPayload(2L, 123456) }.doesNotThrowAnyException()
  }

  @Test
  fun `JWT 페이로드의 제목이 숫자인 경우 파싱`() {
    val payload1 = JwtPayload(1L, "1")
    val payload2 = JwtPayload(2L, 2)

    assertThat(payload1.parseLongSubject()).isEqualTo(1)
    assertThat(payload2.parseLongSubject()).isEqualTo(2)
  }

  @Test
  fun `JWT 페이로드의 제목이 숫자가 아닌 경우 파싱 실패`() {
    val payload = JwtPayload(1L, "subject")

    assertThatThrownBy { payload.parseLongSubject() }.isInstanceOf(NumberFormatException::class.java)
  }
}
