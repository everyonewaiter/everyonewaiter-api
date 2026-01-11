package com.everyonewaiter.domain.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class AuthAttemptTest {

  @Test
  fun `인증 시도 횟수 초과 여부 반환`() {
    AuthPurpose.entries.forEach {
      val attempt = createAuthAttempt(purpose = it)
      assertThat(attempt.isExceed(it.maxAttempt - 1)).isFalse
      assertThat(attempt.isExceed(it.maxAttempt)).isTrue
    }
  }

  @Test
  fun `인증 시도 횟수 저장용 키`() {
    AuthPurpose.entries.forEach {
      val attempt = createAuthAttempt(purpose = it)
      assertThat(attempt.key()).isEqualTo("auth:attempt:${it.lowerCaseName}:${attempt.phoneNumber.value}")
    }
  }

  @Test
  fun `인증 시도 횟수 저장용 값은 사용하지 않음`() {
    val attempt = createAuthAttempt()

    assertThat(attempt.value()).isEqualTo(-2)
  }

  @Test
  fun `인증 시도 횟수의 유효기간은 1일`() {
    val attempt = createAuthAttempt()

    assertThat(attempt.expiration).isEqualTo(Duration.ofDays(1))
  }
}
