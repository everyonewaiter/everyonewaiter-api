package com.everyonewaiter.domain.support

import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import java.time.ZoneId

class TimeZoneTest {

  @Test
  fun `타임존 ID 변환`() {
    TimeZone.entries.forEach {
      assertThatCode { ZoneId.of(it.id) }.doesNotThrowAnyException()
    }
  }
}
