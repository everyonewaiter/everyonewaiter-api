package com.everyonewaiter.domain.support;

import static lombok.AccessLevel.PRIVATE;

import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = PRIVATE)
public final class DateConverter {

  public static Instant convertToUtcStartInstant(TimeZone timeZone, @Nullable String date) {
    if (!StringUtils.hasText(date)) {
      return LocalDate.now(timeZone.zoneId())
          .atStartOfDay(timeZone.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    }

    try {
      return LocalDate.parse(date, DateFormatter.YEAR_MONTH_DAY)
          .atStartOfDay(timeZone.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    } catch (DateTimeParseException exception) {
      throw new BusinessException(ErrorCode.INVALID_DATE_FORMAT);
    }
  }

  public static Instant convertToUtcEndInstant(TimeZone timeZone, @Nullable String date) {
    if (!StringUtils.hasText(date)) {
      return LocalDate.now(timeZone.zoneId())
          .atTime(LocalTime.MAX)
          .atZone(timeZone.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    }

    try {
      return LocalDate.parse(date, DateFormatter.YEAR_MONTH_DAY)
          .atTime(LocalTime.MAX)
          .atZone(timeZone.zoneId())
          .withZoneSameInstant(TimeZone.UTC.zoneId())
          .toInstant();
    } catch (DateTimeParseException exception) {
      throw new BusinessException(ErrorCode.INVALID_DATE_FORMAT);
    }
  }

}
