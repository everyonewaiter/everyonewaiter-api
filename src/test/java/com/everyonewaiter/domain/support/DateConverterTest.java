package com.everyonewaiter.domain.support;

import static com.everyonewaiter.domain.support.TimeZone.ASIA_SEOUL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.global.exception.BusinessException;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class DateConverterTest {

  @Test
  void convertToUtcStartInstant() {
    String kstDate = "20250101";

    Instant utcInstant = DateConverter.convertToUtcStartInstant(ASIA_SEOUL, kstDate);

    assertThat(utcInstant).isEqualTo(Instant.parse("2024-12-31T15:00:00Z"));
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
  void invalidDateFormat() {
    String kstDate = "2025-01-01";

    assertThatThrownBy(() -> DateConverter.convertToUtcStartInstant(ASIA_SEOUL, kstDate))
        .isInstanceOf(BusinessException.class);
    assertThatThrownBy(() -> DateConverter.convertToUtcEndInstant(ASIA_SEOUL, kstDate))
        .isInstanceOf(BusinessException.class);
  }

}
