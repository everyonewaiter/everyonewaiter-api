package com.everyonewaiter.domain.auth

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Duration

class AuthCodeTest {

  @Test
  fun `인증 코드 검증`() {
    val code = createAuthCode()

    assertThatCode { code.verify(code.code()) }.doesNotThrowAnyException()
  }

  @Test
  fun `인증 코드가 만료된 경우 검증 실패`() {
    val code = createAuthCode()

    assertThatThrownBy { code.verify(0) }.isInstanceOf(ExpiredVerificationCodeException::class.java)
  }

  @Test
  fun `인증 코드가 일치하지 않는 경우 검증 실패`() {
    val code = createAuthCode()

    assertThatThrownBy { code.verify(999999) }.isInstanceOf(UnmatchedVerificationCodeException::class.java)
  }

  @Test
  fun `인증 코드 저장용 키`() {
    val code = createAuthCode()

    assertThat(code.key()).isEqualTo("auth:code:${code.phoneNumber.value}")
  }

  @Test
  fun `인증 코드 저장용 값`() {
    val code = createAuthCode()

    assertThat(code.value()).isEqualTo(code.code())
  }

  @Test
  fun `인증 코드의 유효기간은 5분`() {
    val code = createAuthCode()

    assertThat(code.expiration).isEqualTo(Duration.ofMinutes(5))
  }
}
