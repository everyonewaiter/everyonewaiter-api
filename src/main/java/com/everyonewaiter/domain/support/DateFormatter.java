package com.everyonewaiter.domain.support;

import static lombok.AccessLevel.PRIVATE;

import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = PRIVATE)
public final class DateFormatter {

  public static final DateTimeFormatter SERIALIZE =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyyMM");
  public static final DateTimeFormatter YEAR_MONTH_DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

  public static Instant kstDateStringToUtcStartInstant(String date) {
    if (!StringUtils.hasText(date)) {
      return LocalDate.now(TimeZone.ASIA_SEOUL.zoneId())
          .atStartOfDay(TimeZone.ASIA_SEOUL.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    }

    try {
      return LocalDate.parse(date, YEAR_MONTH_DAY)
          .atStartOfDay(TimeZone.ASIA_SEOUL.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    } catch (DateTimeParseException exception) {
      throw new BusinessException(ErrorCode.INVALID_DATE_FORMAT);
    }
  }

  public static Instant kstDateStringToUtcEndInstant(String date) {
    if (!StringUtils.hasText(date)) {
      return LocalDate.now(TimeZone.ASIA_SEOUL.zoneId())
          .atTime(LocalTime.MAX)
          .atZone(TimeZone.ASIA_SEOUL.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    }

    try {
      return LocalDate.parse(date, YEAR_MONTH_DAY)
          .atTime(LocalTime.MAX)
          .atZone(TimeZone.ASIA_SEOUL.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    } catch (DateTimeParseException exception) {
      throw new BusinessException(ErrorCode.INVALID_DATE_FORMAT);
    }
  }

}
