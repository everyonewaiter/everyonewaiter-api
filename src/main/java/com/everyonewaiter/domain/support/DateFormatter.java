package com.everyonewaiter.domain.support;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class DateFormatter {

  public static final DateTimeFormatter SERIALIZE =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyyMM");
  public static final DateTimeFormatter YEAR_MONTH_DAY = DateTimeFormatter.ofPattern("yyyyMMdd");
  public static final DateTimeFormatter YEAR_MONTH_DAY_HOUR_MINUTE_SECOND =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

  public static String formatCurrentKstTime() {
    LocalDateTime now = LocalDateTime.now(TimeZone.ASIA_SEOUL.zoneId());

    return now.format(YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
  }

}
