package com.everyonewaiter.domain.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AuthSuccessTest {

  @Test
  fun `인증 성공 저장용 키`() {
    val success = createAuthSuccess()

    assertThat(success.key()).isEqualTo("auth:success:${success.phoneNumber.value}")
  }

  @Test
  fun `인증 성공 저장용 값은 사용하지 않음`() {
    val success = createAuthSuccess()

    assertThat(success.value()).isEqualTo(-2)
  }

  @Test
  fun `인증 성공의 유효기간은 인증 목적 마다 다름`() {
    AuthPurpose.entries.forEach {
      val success = createAuthSuccess(purpose = it)
      assertThat(success.expiration).isEqualTo(it.expiration)
    }
  }
}
