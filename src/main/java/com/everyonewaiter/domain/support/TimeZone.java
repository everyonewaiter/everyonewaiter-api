package com.everyonewaiter.domain.support;

import java.time.ZoneId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeZone {

  UTC("UTC"),
  ASIA_SEOUL("Asia/Seoul"),
  ;

  private final String id;

  public ZoneId zoneId() {
    return ZoneId.of(id);
  }

}
