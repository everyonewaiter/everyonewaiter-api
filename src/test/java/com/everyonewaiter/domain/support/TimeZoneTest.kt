package com.everyonewaiter.domain.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class TimeZoneTest {

  @Test
  void zoneId() {
    TimeZone[] timeZones = TimeZone.values();

    assertThatCode(() -> {
      for (TimeZone timeZone : timeZones) {
        ZoneId zoneId = timeZone.zoneId();
        assertThat(zoneId).isEqualTo(ZoneId.of(timeZone.getId()));
      }
    }).doesNotThrowAnyException();
  }

}
