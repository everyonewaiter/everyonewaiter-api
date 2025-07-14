package com.everyonewaiter.global.support;

import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateFormatter {

  public static final DateTimeFormatter SERIALIZE =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyyMM");
  public static final DateTimeFormatter YEAR_MONTH_DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

  public static Instant kstDateStringToUtcStartInstant(String date) {
    if (!StringUtils.hasText(date)) {
      return LocalDate.now(ZoneId.of(TimeZone.ASIA_SEOUL.getId()))
          .atStartOfDay(ZoneId.of(TimeZone.ASIA_SEOUL.getId()))
          .withZoneSameInstant(ZoneId.of(TimeZone.UTC.getId()))
          .toInstant();
    }

    try {
      return LocalDate.parse(date, YEAR_MONTH_DAY)
          .atStartOfDay(ZoneId.of(TimeZone.ASIA_SEOUL.getId()))
          .withZoneSameInstant(ZoneId.of(TimeZone.UTC.getId()))
          .toInstant();
    } catch (DateTimeParseException exception) {
      throw new BusinessException(ErrorCode.INVALID_DATE_FORMAT);
    }
  }

  public static Instant kstDateStringToUtcEndInstant(String date) {
    if (!StringUtils.hasText(date)) {
      return LocalDate.now(ZoneId.of(TimeZone.ASIA_SEOUL.getId()))
          .atTime(LocalTime.MAX)
          .atZone(ZoneId.of(TimeZone.ASIA_SEOUL.getId()))
          .withZoneSameInstant(ZoneId.of(TimeZone.UTC.getId()))
          .toInstant();
    }

    try {
      return LocalDate.parse(date, YEAR_MONTH_DAY)
          .atTime(LocalTime.MAX)
          .atZone(ZoneId.of(TimeZone.ASIA_SEOUL.getId()))
          .withZoneSameInstant(ZoneId.of(TimeZone.UTC.getId()))
          .toInstant();
    } catch (DateTimeParseException exception) {
      throw new BusinessException(ErrorCode.INVALID_DATE_FORMAT);
    }
  }

}
