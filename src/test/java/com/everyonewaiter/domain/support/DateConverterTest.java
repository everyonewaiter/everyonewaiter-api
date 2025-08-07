package com.everyonewaiter.domain.support;

import static com.everyonewaiter.domain.support.TimeZone.ASIA_SEOUL;
import static com.everyonewaiter.domain.support.TimeZone.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.domain.shared.BusinessException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class DateConverterTest {

  @Test
  void convertToUtcStartInstant() {
    String kstDate = "20250101";

    Instant utcInstant = DateConverter.convertToUtcStartInstant(ASIA_SEOUL, kstDate);

    assertThat(utcInstant).isEqualTo(Instant.parse("2024-12-31T15:00:00Z"));
  }

  @Test
  void convertToUtcStartInstantInputNull() {
    Instant utcInstant = DateConverter.convertToUtcStartInstant(ASIA_SEOUL, null);

    assertThat(utcInstant)
        .isEqualTo(
            LocalDate.now(ASIA_SEOUL.zoneId())
                .atStartOfDay(ASIA_SEOUL.zoneId())
                .withZoneSameInstant(UTC.zoneId())
                .toInstant()
        );
  }

  @Test
  void convertToUtcEndInstant() {
    String kstDate = "20250101";

    Instant utcInstant = DateConverter.convertToUtcEndInstant(ASIA_SEOUL, kstDate);

    assertThat(utcInstant)
        .isAfter(Instant.parse("2025-01-01T14:59:59Z"))
        .isBefore(Instant.parse("2025-01-01T15:00:00Z"));
  }

  @Test
  void convertToUtcEndInstantInputNull() {
    Instant utcInstant = DateConverter.convertToUtcEndInstant(ASIA_SEOUL, null);

    assertThat(utcInstant)
        .isEqualTo(
            LocalDate.now(ASIA_SEOUL.zoneId())
                .atTime(LocalTime.MAX)
                .atZone(ASIA_SEOUL.zoneId())
                .withZoneSameInstant(UTC.zoneId())
                .toInstant()
        );
  }

  @Test
  void invalidDateFormat() {
    String kstDate = "2025-01-01";

    assertThatThrownBy(() -> DateConverter.convertToUtcStartInstant(ASIA_SEOUL, kstDate))
        .isInstanceOf(BusinessException.class);
    assertThatThrownBy(() -> DateConverter.convertToUtcEndInstant(ASIA_SEOUL, kstDate))
        .isInstanceOf(BusinessException.class);
  }

}
