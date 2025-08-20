package com.everyonewaiter.domain.shared;

import static org.springframework.util.Assert.isTrue;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record BusinessLicense(String value) {

  public static final String BUSINESS_LICENSE_REGEX = "^\\d{3}-\\d{2}-\\d{5}$";

  private static final Pattern BUSINESS_LICENSE_PATTERN = Pattern.compile(BUSINESS_LICENSE_REGEX);

  public BusinessLicense {
    isTrue(BUSINESS_LICENSE_PATTERN.matcher(value).matches(), "사업자 등록 번호 형식이 옳바르지 않습니다: " + value);
  }

}
