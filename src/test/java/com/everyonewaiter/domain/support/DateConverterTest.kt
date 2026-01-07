package com.everyonewaiter.domain.support

import com.everyonewaiter.domain.shared.BusinessException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class DateConverterTest {

  @Test
  fun `문자열 날짜를 UTC 시작 시간으로 변환`() {
    val instant = DateConverter.convertToUtcStartInstant(TimeZone.ASIA_SEOUL, "20250101")

    assertThat(instant).isEqualTo(Instant.parse("2024-12-31T15:00:00Z"))
  }

  @Test
  fun `문자열 날짜가 NULL이라면 오늘 날짜를 UTC 시작 시간으로 변환`() {
    val instant = DateConverter.convertToUtcStartInstant(TimeZone.ASIA_SEOUL, null)

    assertThat(instant).isEqualTo(
      LocalDate.now(TimeZone.ASIA_SEOUL.zoneId())
        .atStartOfDay(TimeZone.ASIA_SEOUL.zoneId())
        .withZoneSameInstant(TimeZone.UTC.zoneId())
        .toInstant()
    )
  }

  @Test
  fun `문자열 날짜의 형식이 옳바르지 않은 경우 UTC 시작 시간으로 변환 실패`() {
    assertThatThrownBy {
      DateConverter.convertToUtcStartInstant(TimeZone.ASIA_SEOUL, "2025-01-01")
    }.isInstanceOf(BusinessException::class.java)
  }

  @Test
  fun `문자열 날짜를 UTC 마지막 시간으로 변환`() {
    val instant = DateConverter.convertToUtcEndInstant(TimeZone.ASIA_SEOUL, "20250101")

    assertThat(instant)
      .isAfter(Instant.parse("2025-01-01T14:59:59Z"))
      .isBefore(Instant.parse("2025-01-01T15:00:00Z"))
  }

  @Test
  fun `문자열 날짜가 NULL이라면 오늘 날짜를 UTC 마지막 시간으로 변환`() {
    val instant = DateConverter.convertToUtcEndInstant(TimeZone.ASIA_SEOUL, null)

    assertThat(instant).isEqualTo(
      LocalDate.now(TimeZone.ASIA_SEOUL.zoneId())
        .atTime(LocalTime.MAX)
        .atZone(TimeZone.ASIA_SEOUL.zoneId())
        .withZoneSameInstant(TimeZone.UTC.zoneId())
        .toInstant()
    )
  }

  @Test
  fun `문자열 날짜의 형식이 옳바르지 않은 경우 UTC 마지막 시간으로 변환 실패`() {
    assertThatThrownBy {
      DateConverter.convertToUtcEndInstant(TimeZone.ASIA_SEOUL, "2025-01-01")
    }.isInstanceOf(BusinessException::class.java)
  }
}
