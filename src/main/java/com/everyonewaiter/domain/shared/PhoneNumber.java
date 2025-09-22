package com.everyonewaiter.domain.shared;

import static org.springframework.util.Assert.isTrue;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record PhoneNumber(String value) {

  public static final String PHONE_NUMBER_REGEX = "^01[016789]\\d{8}$";

  private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

  public PhoneNumber {
    isTrue(PHONE_NUMBER_PATTERN.matcher(value).matches(), "휴대폰 번호 형식이 옳바르지 않습니다: " + value);
  }

}
