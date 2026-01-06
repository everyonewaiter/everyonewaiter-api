package com.everyonewaiter.domain.shared

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BusinessLicenseTest {

  @Test
  fun `사업자 등록 번호 생성`() {
    val value = "443-60-00875"

    val businessLicense = BusinessLicense(value)

    assertThat(businessLicense.value).isEqualTo(value)
  }

  @Test
  fun `사업자 등록 번호의 형식이 옳바르지 않은 경우 생성 실패`() {
    val value = "4436000875"

    assertThatThrownBy { BusinessLicense(value) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
